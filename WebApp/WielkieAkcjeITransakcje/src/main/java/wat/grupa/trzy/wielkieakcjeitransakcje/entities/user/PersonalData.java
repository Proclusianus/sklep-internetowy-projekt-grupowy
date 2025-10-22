package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.PersonalPayment;

import java.util.List;

public class PersonalData {
    private String name;
    private String surname;
    private List<Address> addresses;
    private PersonalPayment paymentInformation;
    private String phoneNumber;

    public PersonalData(String name, String surname, List<Address> addresses, PersonalPayment paymentInformation, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.addresses = addresses;
        this.paymentInformation = paymentInformation;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public List<Address> getAddresses() { return addresses; }
    public PersonalPayment getPaymentInformation() { return paymentInformation; }
    public String getPhoneNumber() { return phoneNumber; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public void setPaymentInformation(PersonalPayment paymentInformation) { this.paymentInformation = paymentInformation; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
