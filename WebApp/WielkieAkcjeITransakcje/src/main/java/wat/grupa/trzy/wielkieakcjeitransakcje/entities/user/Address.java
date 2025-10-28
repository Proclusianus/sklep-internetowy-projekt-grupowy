package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private ADDRESS_TYPE addressType;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "zip_code")
    private String zipcode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    // Adres domowy osoby indywidualnej
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_data_id")
    private PersonalData personalData;

    // Adres firmy
    @OneToOne(mappedBy = "hqAddress", fetch = FetchType.LAZY)
    private BusinessData businessDataHQ;

    // Adresy dostawcze firmy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_delivery_address_id")
    private BusinessData businessDataDelivery;

    public Address() {}
    public Address(String street, String houseNumber, String apartmentNumber,
                   String zipcode, String city, String country, PersonalData personalData)
    {
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.personalData =personalData;
    }

    // Getters
    public ADDRESS_TYPE getAddressType() { return addressType; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public String getZipcode() { return zipcode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    //public PersonalData getPersonalData() { return personalData; }

    // Setters
    public void setAddressType(ADDRESS_TYPE addressType) { this.addressType = addressType; }
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    //public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }
}
