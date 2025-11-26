package wat.grupa.trzy.wielkieakcjeitransakcje.services.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_ContactPerson;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Registration;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.user.DTO_Address;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.BankAccountData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.CONTACT_PERSON_TYPE;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.PasswordValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.UserNameValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business.BusinessNameValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business.KrsIdValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business.NipValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal.EmailValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal.PhoneNumberValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal.FirstNameValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal.SurnameValidator;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.ConfirmationTokenRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.EmailService;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class RegistrationService {
    private final UserDataRepository userDataRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    public RegistrationService(UserDataRepository userDataRepository, ConfirmationTokenRepository confirmationTokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userDataRepository = userDataRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /////////////////////////
    // FUNKCJE BIZNESOWE
    /////////////////////////

    // Rejestracja użytkownika
    /* Najpierw zostanie sprawdzona poprawnosc danych, tj.
     - wspólne dane: niepowtarzalność emaila, username'a,
                     hasło musi być poprawnie powtórzone dwukrotnie, min. 7 znaków,
                     musi mieć mały znak, duży znak, liczbę i znak specjalny

       ADRESY POBIERANE Z BD TERYT, NIE SĄ SPRAWDZANE
     - dane osoby:   tutaj w zasadzie tylko trzeba sprawdzić czy number składa się z
                     cyfr i znaków specjalnych

     - dane firmy:   name firmy, nip, krs muszą być unikatowe, ContactPerson ma mieć
                     poprawny numer telefonu.

       Pozostała walidacja danych odbywa się na zasadzie walidacji formatu, czyli
       np. sprawdzanie długości stringa, czy zawiera litery, cyfry czy znaki specjalne.
       Po szczegóły dla każdego jednego pola, trzeba zajrzeć do odpowiedniego walidatora.

       Mając już zweryfikowane dane, trzeba zhashować hasło, utworzyć instancję
       userData no i ją zapisać.

       Funkcja zwraca wiadomość zwrotną na stronę z uwagami co do wprowadzonych
       danych (jeżeli coś jest źle).
     */
    @Transactional
    public void registerNewUser(DTO_Registration request) {
        validateInput(request);
        String passwordHash = passwordEncoder.encode(request.getPassword());
        UserData newUser = createUserData(request, passwordHash);
        userDataRepository.save(newUser);
        String tokenString = newUser.getConfirmationTokens().getFirst().getToken();
        sendConfirmationEmail(newUser.getUsername(), newUser.getEmail(), tokenString);
    }

    @Transactional
    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token nie znaleziony"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email został już potwierdzony");
        }
        OffsetDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
            throw new IllegalStateException("Token wygasł");
        }

        confirmationToken.setConfirmedAt(OffsetDateTime.now(ZoneOffset.UTC));
        UserData user = confirmationToken.getUserData();
        user.setEnabled(true);
        userDataRepository.save(user);
        confirmationTokenRepository.delete(confirmationToken);
    }

    /////////////////////
    // FUNKCJE MAILOWE //
    /////////////////////
    public void sendConfirmationEmail(String userName, String userEmail, String tokenString) {
        String confirmationLink = baseUrl + "/api/confirm-account?token=" + tokenString;

        String mailSubject = "Potwierdź swoją rejestrację - Wielkie Akcje i Transakcje";
        String mailContent = "<h1>Witaj, " + userName + "!</h1>" +
                "<p>Dziękujemy za rejestrację w serwisie Wielkich Akcji i Transakcji. Aktywuj swoje konto klikając w poniższy link:</p>" +
                "<a href=\"" + confirmationLink + "\">Aktywuj konto</a>";

        emailService.sendHtmlMessage(userEmail, mailSubject, mailContent);
    }

    /////////////////////////
    // FUNKCJE WALIDACJI
    /////////////////////////
    public void validateInput(DTO_Registration request) {
        StringBuilder errorBuilder = new StringBuilder();

        subvalidateCommonData(request.getEmail(), request.getUsername(), request.getPassword(), request.getConfirmPassword(), errorBuilder);
        if ("osoba".equals(request.getAccountType()))
            subvalidatePersonalData(request.getFirstName(), request.getLastName(), request.getAddress(), request.getPhoneNumber(), errorBuilder);
        else if ("firma".equals(request.getAccountType()))
            subvalidateBusinessData(request.getFirmName(), request.getHqAddress(), request.getNip(), request.getKrs(), request.getContactPerson(),
                    request.getDeliveryAddresses(), errorBuilder);

        if (errorBuilder.length() > 0) {
            String finalErrorMessage = errorBuilder.toString();
            throw new IllegalArgumentException(finalErrorMessage);
        }
    }

    public void subvalidateField(Runnable validationLogic, StringBuilder eb) {
        try {
            validationLogic.run();
        } catch (IllegalArgumentException e) {
            eb.append(e.getMessage()).append('\n');
        }
    }

    public void subvalidateCommonData(String email, String username, String password, String confirmPassword, StringBuilder eb) {
        if (userDataRepository.existsByEmail(email)) { eb.append("Email jest już zajęty.\n"); }
        subvalidateField(() -> EmailValidator.validate(email), eb);
        if (userDataRepository.existsByUsername(username)) { eb.append("Ta name użytkownika jest już zajęta\n"); }
        subvalidateField(() -> UserNameValidator.validate(username), eb);
        subvalidateField(() -> PasswordValidator.validate(password, confirmPassword), eb);
    }

    public void subvalidateAddress(DTO_Address address, StringBuilder eb) {
        // Wspólne pola
        subvalidateField(() -> ZipcodeValidator.validate(address.getZipCode()), eb);
        subvalidateField(() -> LocaleNumberValidator.validate(address.getHouseNumber()), eb);
        subvalidateField(() -> ApartmentNumberValidator.validate(address.getApartmentNumber()), eb);
        subvalidateField(() -> StreetValidator.validate(address.getStreet()), eb);

        // Pola dla adresu zagranicznego
        if (!Objects.equals(address.getCountry(), "POLSKA")) {
            subvalidateField(() -> ForeignCityValidator.validate(address.getCity()), eb);
            if (StringUtils.hasText(address.getAdminLevel1())) {
                subvalidateField(() -> ForeignAdminUnitValidator.validate(address.getAdminLevel1()), eb);
            }
            if (StringUtils.hasText(address.getAdminLevel2())) {
                subvalidateField(() -> ForeignAdminUnitValidator.validate(address.getAdminLevel2()), eb);
            }
            if (StringUtils.hasText(address.getAdminLevel3())) {
                subvalidateField(() -> ForeignAdminUnitValidator.validate(address.getAdminLevel3()), eb);
            }
            if (StringUtils.hasText(address.getAdminLevel4())) {
                subvalidateField(() -> ForeignAdminUnitValidator.validate(address.getAdminLevel4()), eb);
            }}
    }

    public void subvalidatePersonalData(String firstName, String lastName, DTO_Address address, String phoneNumber, StringBuilder eb) {
        subvalidateField(() -> FirstNameValidator.validate(firstName), eb);
        subvalidateField(() -> SurnameValidator.validate(lastName), eb);
        subvalidateAddress(address, eb);
        subvalidateField(() -> PhoneNumberValidator.validate(phoneNumber), eb);
    }

    public void subvalidateContactPerson(String name, String surname, String email, String phoneNumber, StringBuilder eb) {
        subvalidateField(() -> FirstNameValidator.validate(name), eb);
        subvalidateField(() -> SurnameValidator.validate(surname), eb);
        if (userDataRepository.existsByEmail(email)) { eb.append("Email jest już zajęty.\n"); }
        subvalidateField(() -> EmailValidator.validate(email), eb);
        subvalidateField(() -> PhoneNumberValidator.validate(phoneNumber), eb);
    }

    public void subvalidateBusinessData(String firmName, DTO_Address hqAddress, String nip, String krs,
                                        DTO_ContactPerson contactPerson, List<DTO_Address> deliveryAddresses, StringBuilder eb) {
        if (userDataRepository.existsByBusinessDataFirmName(firmName)) { eb.append("Name firmy jest już zajęta\n"); }
        subvalidateField(() -> BusinessNameValidator.validate(firmName), eb);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        // No oczywiście by trzeba było też dostać się do BD urzędu i potwierdzić, że to się zgadza;
        // ale to projekt w ramach ćwiczenia z bardzo ograniczonym deadlinem, więc nie ma to większego sensu
        if (userDataRepository.existsByBusinessDataNip(nip)) { eb.append("Numer NIP jest już zajęty\n"); }
        subvalidateField(() -> NipValidator.validate(nip), eb);
        if (userDataRepository.existsByBusinessDataKrsId(krs)) { eb.append("Numer KRS jest już zajęty\n"); }
        subvalidateField(() -> KrsIdValidator.validate(krs), eb);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        subvalidateContactPerson(contactPerson.getName(), contactPerson.getSurname(), contactPerson.getEmail(), contactPerson.getPhoneNumber(), eb);
        for (DTO_Address address : deliveryAddresses) {
            subvalidateAddress(address, eb);
        }
    }

    //////////////////////////////
    // FUNKCJE TWORZENIA ENCJI
    //////////////////////////////
    public UserData createUserData(DTO_Registration request, String passwordHash) {
        UserData t = new UserData();
        t.setUsername(request.getUsername());
        t.setEmail(request.getEmail());
        t.setPasswordHash(passwordHash);
        t.setWallet(createWallet(t));

        if ("osoba".equals(request.getAccountType())) {
            t.setAccountType(E_TYP_KONTA.OSOBA_FIZYCZNA);
            t.setPersonalData(createPersonalData(request));
        } else if ("firma".equals(request.getAccountType())) {
            t.setAccountType(E_TYP_KONTA.FIRMA);
            t.setBusinessData(createBusinessData(request));
        }

        t.setConfirmationTokens(new ArrayList<ConfirmationToken>());
        t.getConfirmationTokens().add(createUserConfirmationToken(t));
        t.setEnabled(false);

        return t;
    }

    public Wallet createWallet(UserData newUser) {
        Wallet w = new Wallet();
        w.setUserData(newUser);
        w.setOwnedCards(new ArrayList<CardData>());
        w.setOwnedBankAccounts(new ArrayList<BankAccountData>());
        w.setTransactionDataList(new ArrayList<TransactionData>());

        return w;
    }

    public ConfirmationToken createUserConfirmationToken(UserData newUser) {
        String token = UUID.randomUUID().toString();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                now,
                now.plusMinutes(15),
                newUser
        );
        return confirmationToken;
    }

    public Address createAddressData(DTO_Address dto_address, ADDRESS_TYPE addressType, Object parent) {
        Address a = new Address();
        a.setAddressType(addressType);
        a.setStreet(dto_address.getStreet());
        a.setHouseNumber(dto_address.getHouseNumber());
        a.setApartmentNumber(dto_address.getApartmentNumber());
        a.setZipcode(dto_address.getZipCode());
        a.setCity(dto_address.getCity());
        a.setCountry(dto_address.getCountry());
        a.setAdministrativeAreaLevel1(dto_address.getAdminLevel1());
        a.setAdministrativeAreaLevel2(dto_address.getAdminLevel2());
        a.setAdministrativeAreaLevel3(dto_address.getAdminLevel3());
        a.setAdministrativeAreaLevel4(dto_address.getAdminLevel4());
        if (parent instanceof PersonalData) {
            a.setPersonalData((PersonalData) parent);
        } else if (parent instanceof BusinessData) {
            if (addressType == ADDRESS_TYPE.ADRES_FIRMY)
                a.setBusinessDataHQ((BusinessData) parent);
            else if (addressType == ADDRESS_TYPE.ADRESY_DOSTAWCZE_FIRMY)
                a.setBusinessDataDelivery((BusinessData) parent);
        }

        return a;
    }

    public PersonalData createPersonalData(DTO_Registration request) {
        PersonalData ti = new PersonalData();
        ti.setName(request.getFirstName());
        ti.setSurname(request.getLastName());
        ti.setPhoneNumber(request.getPhoneNumber());
        ti.getAddresses().add(createAddressData(request.getAddress(), ADDRESS_TYPE.ADRES_DOMOWY, ti));

        return ti;
    }

    public BusinessData createBusinessData(DTO_Registration request) {
        BusinessData tf = new BusinessData();
        tf.setFirmName(request.getFirmName());
        tf.setNip(request.getNip());
        tf.setKrsId(request.getKrs());
        tf.setHqAddress(createAddressData(request.getHqAddress(), ADDRESS_TYPE.ADRES_FIRMY, tf));

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
            tf.getDeliveryAddresses().add(createAddressData(request.getDeliveryAddresses().get(i), ADDRESS_TYPE.ADRESY_DOSTAWCZE_FIRMY, tf));
        }

        return tf;
    }
}
