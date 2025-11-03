package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Registration;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.CONTACT_PERSON_TYPE;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.PasswordValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.PhoneNumberValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.UserDataRepository;

import java.util.ArrayList;

@Service
public class RegistrationService {
    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserDataRepository userDataRepository, PasswordEncoder passwordEncoder) {
        this.userDataRepository = userDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validateInput(DTO_Registration request) {
        boolean throwException = false;
        String errorMsg = "";

        // Sprawdzanie wspólnych danych:
        if (userDataRepository.existsByEmail(request.getEmail())) { errorMsg += "Email jest już zajęty.\n"; throwException = true; }
        if (userDataRepository.existsByUsername(request.getUsername())) { errorMsg += "Ta nazwa użytkownika jest już zajęta\n"; throwException = true; }
        try {
            PasswordValidator.validate(request.getPassword(), request.getConfirmPassword());
        } catch (IllegalArgumentException e) {
            errorMsg += e.getMessage() + '\n';
            throwException = true;
        }

        // Sprawdzanie danych osoby indywidualnej:
        if ("osoba".equals(request.getAccountType())) {
            try {
                PhoneNumberValidator.validate(request.getPhoneNumber());
            } catch (IllegalArgumentException e) {
                errorMsg += e.getMessage() + '\n';
                throwException = true;
            }
        }
        // Sprawdzanie danych firmy
        else if ("firma".equals(request.getAccountType())) {
            if (userDataRepository.existsByBusinessDataFirmName(request.getFirmName())) { errorMsg += "Nazwa firmy jest już zajęta\n"; throwException = true; }

            // No oczywiście by trzeba było też dostać się do BD urzędu i potwierdzić, że to się zgadza;
            // ale to projekt w ramach ćwiczenia, więc nie ma to większego sensu
            if (userDataRepository.existsByBusinessDataNip(request.getNip())) { errorMsg += "Numer NIP jest już zajęty\n"; throwException = true; }
            if (userDataRepository.existsByBusinessDataKrsId(request.getKrs())) { errorMsg += "Numer KRS jest już zajęty\n"; throwException = true; }
            try {
                PhoneNumberValidator.validate(request.getContactPerson().getPhoneNumber());
            } catch (IllegalArgumentException e) {
                errorMsg += "Osoba kontaktowa" + e.getMessage() + '\n';
                throwException = true;
            }
        }

        if (throwException)
            throw new IllegalStateException(errorMsg);
    }

    public PersonalData createPersonalData(DTO_Registration request) {
        PersonalData ti = new PersonalData();
        ti.setName(request.getFirstName());
        ti.setSurname(request.getLastName());
        ti.setPhoneNumber(request.getPhoneNumber());

        Address tia = new Address();
        tia.setAddressType(ADDRESS_TYPE.ADRES_DOMOWY);
        tia.setStreet(request.getAddress().getStreet());
        tia.setHouseNumber(request.getAddress().getHouseNumber());
        tia.setApartmentNumber(request.getAddress().getApartmentNumber());
        tia.setZipcode(request.getAddress().getZipCode());
        tia.setCity(request.getAddress().getCity());
        tia.setCountry(request.getAddress().getCountry());
        tia.setPersonalData(ti);

        ti.getAddresses().add(tia);

        return ti;
    }

    public BusinessData createBusinessData(DTO_Registration request) {
        BusinessData tf = new BusinessData();
        tf.setFirmName(request.getFirmName());
        tf.setNip(request.getNip());
        tf.setKrsId(request.getKrs());

        Address tfa = new Address();
        tfa.setAddressType(ADDRESS_TYPE.ADRES_FIRMY);
        tfa.setStreet(request.getHqAddress().getStreet());
        tfa.setHouseNumber(request.getHqAddress().getHouseNumber());
        tfa.setApartmentNumber(request.getHqAddress().getApartmentNumber());
        tfa.setZipcode(request.getHqAddress().getZipCode());
        tfa.setCity(request.getHqAddress().getCity());
        tfa.setCountry(request.getHqAddress().getCountry());
        tfa.setBusinessDataHQ(tf);
        tf.setHqAddress(tfa);

        ContactPerson tfp = new ContactPerson();
        tfp.setContactPersonType(CONTACT_PERSON_TYPE.FIRMA);
        tfp.setName(request.getContactPerson().getName());
        tfp.setSurname(request.getContactPerson().getSurname());
        tfp.setEmail(request.getContactPerson().getEmail());
        tfp.setPhoneNumber(request.getContactPerson().getPhoneNumber());
        tfp.setBusinessData(tf);
        tf.setContactPersonFirm(tfp);

        tf.setDeliveryAddresses(new ArrayList<Address>());
        for (int i=0; i < request.getDeliveryAddresses().size(); ++i) {
            Address tfda = new Address();
            tfda.setAddressType(ADDRESS_TYPE.ADRESY_DOSTAWCZE_FIRMY);
            tfda.setStreet(request.getDeliveryAddresses().get(i).getStreet());
            tfda.setHouseNumber(request.getDeliveryAddresses().get(i).getHouseNumber());
            tfda.setApartmentNumber(request.getDeliveryAddresses().get(i).getApartmentNumber());
            tfda.setZipcode(request.getDeliveryAddresses().get(i).getZipCode());
            tfda.setCity(request.getDeliveryAddresses().get(i).getCity());
            tfda.setCountry(request.getDeliveryAddresses().get(i).getCountry());
            tfda.setBusinessDataDelivery(tf);
            tf.getDeliveryAddresses().add(tfda);
        }

        return tf;
    }

    public UserData createUserData(DTO_Registration request, String passwordHash) {
        UserData t = new UserData();
        t.setUsername(request.getUsername());
        t.setEmail(request.getEmail());
        t.setPasswordHash(passwordHash);

        if ("osoba".equals(request.getAccountType())) {
            t.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);
            t.setPersonalData(createPersonalData(request));
        } else if ("firma".equals(request.getAccountType())) {
            t.setAccountType(E_TYP_KONTA.FIRMA);
            t.setBusinessData(createBusinessData(request));
        }

        return t;
    }

    // Rejestracja użytkownika
    /* Najpierw zostanie sprawdzona poprawnosc danych, tj.
     - wspólne dane: niepowtarzalność emaila, username'a,
                     hasło musi być poprawnie powtórzone dwukrotnie, min. 7 znaków,
                     musi mieć mały znak, duży znak, liczbę i znak specjalny

       ADRESY POBIERANE Z BD TERYT, NIE SĄ SPRAWDZANE
     - dane osoby:   tutaj w zasadzie tylko trzeba sprawdzić czy number składa się z
                     cyfr i znaków specjalnych

     - dane firmy:   nazwa firmy, nip, krs muszą być unikatowe, ContactPerson ma mieć
                     poprawny numer telefonu.

       Mając już zweryfikowane dane, trzeba zhashować hasło, utworzyć instancję
       userData no i ją zapisać.

       Funkcja zwraca wiadomość zwrotną na stronę z uwagami co do wprowadzonych
       danych (jeżeli coś jest źle)
     */
    public void registerNewUser(DTO_Registration request) {
        validateInput(request);
        String passwordHash = passwordEncoder.encode(request.getPassword());
        userDataRepository.save(createUserData(request,passwordHash));
    }
}
