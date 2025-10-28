package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.CONTACT_PERSON_TYPE;

@Entity
@Table(name = "contact_person")
public class ContactPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_person_type")
    @Enumerated(EnumType.STRING)
    private CONTACT_PERSON_TYPE contactPersonType;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    // Odwołanie do admina
    @OneToOne(mappedBy = "contactPersonAdmin", fetch = FetchType.LAZY)
    private AdminData adminData;

    // Do osoby kontaktowej w firmie
    @OneToOne(mappedBy = "contactPersonFirm", fetch = FetchType.LAZY)
    private BusinessData businessData;

    public ContactPerson() {}
    public ContactPerson(String n, String sn, String e, String pn) {
        this.name = n;
        this.surname = sn;
        this.email = e;
        this.phoneNumber = pn;
    }

    // Getters
    public CONTACT_PERSON_TYPE getContactPersonType() { return contactPersonType; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }

    // Setters
    public void setContactPersonType(CONTACT_PERSON_TYPE contactPersonType) { this.contactPersonType = contactPersonType; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
