package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "administrative_area_level_1")
    private String administrativeAreaLevel1;

    @Column(name = "administrative_area_level_2")
    private String administrativeAreaLevel2;

    @Column(name = "administrative_area_level_3")
    private String administrativeAreaLevel3;

    @Column(name = "administrative_area_level_4")
    private String administrativeAreaLevel4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_data_id")
    private PersonalData personalData;

    @OneToOne(mappedBy = "hqAddress", fetch = FetchType.LAZY)
    private BusinessData businessDataHQ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_delivery_address_id")
    private BusinessData businessDataDelivery;

    public Address() {}

    public ADDRESS_TYPE getAddressType() { return addressType; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public String getZipcode() { return zipcode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public PersonalData getPersonalData() { return personalData; }
    public BusinessData getBusinessDataHQ() { return businessDataHQ; }
    public BusinessData getBusinessDataDelivery() { return businessDataDelivery; }
    public String getAdministrativeAreaLevel1() { return administrativeAreaLevel1; }
    public String getAdministrativeAreaLevel2() { return administrativeAreaLevel2; }
    public String getAdministrativeAreaLevel3() { return administrativeAreaLevel3; }
    public String getAdministrativeAreaLevel4() { return administrativeAreaLevel4; }

    public void setAddressType(ADDRESS_TYPE addressType) { this.addressType = addressType; }
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }
    public void setBusinessDataHQ(BusinessData businessDataHQ) { this.businessDataHQ = businessDataHQ; }
    public void setBusinessDataDelivery(BusinessData businessDataDelivery) { this.businessDataDelivery = businessDataDelivery; }
    public void setAdministrativeAreaLevel1(String administrativeAreaLevel1) { this.administrativeAreaLevel1 = administrativeAreaLevel1; }
    public void setAdministrativeAreaLevel2(String administrativeAreaLevel2) { this.administrativeAreaLevel2 = administrativeAreaLevel2; }
    public void setAdministrativeAreaLevel3(String administrativeAreaLevel3) { this.administrativeAreaLevel3 = administrativeAreaLevel3; }
    public void setAdministrativeAreaLevel4(String administrativeAreaLevel4) { this.administrativeAreaLevel4 = administrativeAreaLevel4; }
}
