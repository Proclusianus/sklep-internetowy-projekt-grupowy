package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.PersonalPayment;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "personal_data")
public class PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_information_id")
    private PersonalPayment paymentInformation;

    @OneToMany(mappedBy = "personalData", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public PersonalData() {}
    public PersonalData(String name, String surname, String phoneNumber) {
        this.name = name;
        this.surname = surname;
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