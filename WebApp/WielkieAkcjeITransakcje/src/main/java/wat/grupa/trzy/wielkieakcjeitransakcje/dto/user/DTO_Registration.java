package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Address;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.ContactPerson;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import java.util.List;

import static wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA.FIRMA;
import static wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA.OSOBA_FIZYCZNA;

public class DTO_Registration {
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private String accountType;

    // Osoba fizyczna
    private String firstName;
    private String lastName;
    private DTO_Address address;
    private String phoneNumber;

    // Firma
    private String firmName;
    private DTO_Address hqAddress;
    private String nip;
    private String krs;
    private DTO_ContactPerson contactPerson;
    private List<DTO_Address> deliveryAddresses;

    public DTO_Registration() {}
    /* Pod osobę fizyczną
    public DTO_Registration(String email, String username, String password, String firstName, String lastName, Address address, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.accountType = OSOBA_FIZYCZNA;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Pod firmę
    public DTO_Registration(String email, String username, String password, String firmName, Address hqAddress, String nip, String krs, ContactPerson contactPerson, List<DTO_Address> deliveryAddresses) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.accountType = FIRMA;
        this.firmName = firmName;
        this.hqAddress = hqAddress;
        this.nip = nip;
        this.krs = krs;
        this.contactPerson = contactPerson;
        this.deliveryAddresses = deliveryAddresses;
    }*/

    // Gettery
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getAccountType() { return accountType; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public DTO_Address getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFirmName() { return firmName; }
    public DTO_Address getHqAddress() { return hqAddress; }
    public String getNip() { return nip; }
    public String getKrs() { return krs; }
    public DTO_ContactPerson getContactPerson() { return contactPerson; }
    public List<DTO_Address> getDeliveryAddresses() { return deliveryAddresses; }

    // Settery
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAddress(DTO_Address address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setFirmName(String firmName) { this.firmName = firmName; }
    public void setHqAddress(DTO_Address hqAddress) { this.hqAddress = hqAddress; }
    public void setNip(String nip) { this.nip = nip; }
    public void setKrs(String krs) { this.krs = krs; }
    public void setContactPerson(DTO_ContactPerson contactPerson) { this.contactPerson = contactPerson; }
    public void setDeliveryAddresses(List<DTO_Address> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
}
