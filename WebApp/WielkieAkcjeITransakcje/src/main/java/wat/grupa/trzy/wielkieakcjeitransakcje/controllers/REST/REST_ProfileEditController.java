package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_UserProfileEditView;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_UserUpdateForm;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Address_Edit_User_Data;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class REST_ProfileEditController {

    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public REST_ProfileEditController(UserDataRepository userDataRepository, PasswordEncoder passwordEncoder) {
        this.userDataRepository = userDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUserForEdit() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(401).body("Użytkownik nie jest zalogowany");
        }

        var userOpt = userDataRepository.findByEmail(auth.getName());
        if(userOpt.isEmpty()) {
            userOpt = userDataRepository.findByUsername(auth.getName());
        }

        return userOpt
                .map(user -> ResponseEntity.ok(new DTO_UserProfileEditView(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<?> updateUser(@RequestBody DTO_UserUpdateForm form) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Brak autoryzacji");
        }

        var userOpt = userDataRepository.findByEmail(auth.getName());
        if(userOpt.isEmpty()) userOpt = userDataRepository.findByUsername(auth.getName());

        return userOpt.map(user -> {
            if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
                if (form.getCurrentPassword() == null || form.getCurrentPassword().isBlank()) {
                    return ResponseEntity.badRequest().body("Aby zmienić hasło, musisz podać obecne hasło.");
                }
                if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPasswordHash())) {
                    return ResponseEntity.badRequest().body("Podane obecne hasło jest nieprawidłowe.");
                }
                user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
            }

            if ("INDIVIDUAL".equals(form.getAccountType())) {
                if (user.getPersonalData() == null) user.setPersonalData(new PersonalData());
                var entity = user.getPersonalData();
                var dto = form.getPersonalDetails();

                if (dto != null) {
                    if (dto.getFirstName() != null) entity.setName(dto.getFirstName());
                    if (dto.getLastName() != null) entity.setSurname(dto.getLastName());
                    if (dto.getPhoneNumber() != null) entity.setPhoneNumber(dto.getPhoneNumber());
                }

                if (form.getMainAddress() != null) {
                    if (entity.getAddresses() == null) entity.setAddresses(new ArrayList<>());

                    Address addr = entity.getAddresses().stream()
                            .filter(a -> a.getAddressType() == ADDRESS_TYPE.ADRES_DOMOWY)
                            .findFirst()
                            .orElseGet(() -> {
                                Address a = new Address();
                                a.setAddressType(ADDRESS_TYPE.ADRES_DOMOWY);
                                a.setPersonalData(entity);
                                entity.getAddresses().add(a);
                                return a;
                            });

                    updateAddressFromDto(addr, form.getMainAddress());
                }
            }

            if ("COMPANY".equals(form.getAccountType())) {
                if (user.getBusinessData() == null) user.setBusinessData(new BusinessData());
                var entity = user.getBusinessData();
                var dto = form.getBusinessDetails();

                if (dto != null) {
                    if (dto.getCompanyName() != null) entity.setFirmName(dto.getCompanyName());
                    if (dto.getNip() != null) entity.setNip(dto.getNip());
                    if (dto.getKrs() != null) entity.setKrsId(dto.getKrs());

                    if (entity.getContactPerson() == null) entity.setContactPersonFirm(new ContactPerson());
                    var contact = entity.getContactPerson();
                    if (dto.getContactFirstName() != null) contact.setName(dto.getContactFirstName());
                    if (dto.getContactLastName() != null) contact.setSurname(dto.getContactLastName());
                    if (dto.getContactEmail() != null) contact.setEmail(dto.getContactEmail());
                    if (dto.getContactPhone() != null) contact.setPhoneNumber(dto.getContactPhone());
                }

                if (form.getMainAddress() != null) {
                    Address hq = entity.getHqAddress();
                    if (hq == null) {
                        hq = new Address();
                        hq.setAddressType(ADDRESS_TYPE.ADRES_FIRMY);
                        entity.setHqAddress(hq);
                    }
                    updateAddressFromDto(hq, form.getMainAddress());
                }

                if (form.getDeliveryAddresses() != null) {
                    if (entity.getDeliveryAddresses() == null) entity.setDeliveryAddresses(new ArrayList<>());
                    entity.getDeliveryAddresses().clear();

                    for (DTO_Address_Edit_User_Data dtoAddr : form.getDeliveryAddresses()) {
                        Address del = new Address();
                        updateAddressFromDto(del, dtoAddr);
                        del.setAddressType(ADDRESS_TYPE.ADRESY_DOSTAWCZE_FIRMY);
                        del.setBusinessDataDelivery(entity);
                        entity.getDeliveryAddresses().add(del);
                    }
                }
            }

            userDataRepository.save(user);
            return ResponseEntity.ok().body("{\"message\": \"Zaktualizowano pomyślnie\"}");

        }).orElseGet(() -> ResponseEntity.status(404).body("{\"message\": \"Nie znaleziono użytkownika\"}"));
    }

    private void updateAddressFromDto(Address entity, DTO_Address_Edit_User_Data dto) {
        entity.setStreet(dto.getStreet());
        entity.setHouseNumber(dto.getHouseNumber());
        entity.setApartmentNumber(dto.getApartmentNumber());
        entity.setZipcode(dto.getZipCode());
        entity.setCity(dto.getCity());
        entity.setCountry(dto.getCountry());
    }
}