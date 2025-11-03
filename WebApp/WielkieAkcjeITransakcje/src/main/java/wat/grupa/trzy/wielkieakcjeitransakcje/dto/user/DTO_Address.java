package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import jakarta.persistence.Column;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;

public class DTO_Address {
    private ADDRESS_TYPE addressType;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String zipCode;
    private String city;
    private String country;

    public DTO_Address() {}
    /*
    public DTO_Address(ADDRESS_TYPE addressType, String street, String buildingLocale, String zipCode, String city, String country) {
        this.addressType = addressType;
        this.street = street;
        this.buildingLocale = buildingLocale;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }*/

    // Gettery
    public ADDRESS_TYPE getAddressType() { return addressType; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public String getZipCode() { return zipCode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    // Settery
    public void setAddressType(ADDRESS_TYPE addressType) { this.addressType = addressType; }
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
}
