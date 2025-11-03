package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import jakarta.persistence.Column;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.CONTACT_PERSON_TYPE;

public class DTO_ContactPerson {
    private CONTACT_PERSON_TYPE contactPersonType;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    public DTO_ContactPerson() {}
    /*
    public DTO_ContactPerson(CONTACT_PERSON_TYPE contactPersonType, String name, String surname, String email, String phoneNumber) {
        this.contactPersonType = contactPersonType;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }*/

    // Gettery
    public CONTACT_PERSON_TYPE getContactPersonType() { return contactPersonType; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }

    // Settery
    public void setContactPersonType(CONTACT_PERSON_TYPE contactPersonType) { this.contactPersonType = contactPersonType; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
