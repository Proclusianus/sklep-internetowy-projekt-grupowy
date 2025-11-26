package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import jakarta.persistence.Column;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Address;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.ADDRESS_TYPE;

public class DTO_Address {
    private ADDRESS_TYPE addressType;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String zipCode;
    private String city;
    private String country;
    private String adminLevel1;
    private String adminLevel2;
    private String adminLevel3;
    private String adminLevel4;

    public DTO_Address() {}

    public DTO_Address(Address address) {
        if (address != null) {
            this.street = address.getStreet();
            this.houseNumber = address.getHouseNumber();
            this.apartmentNumber = address.getApartmentNumber();
            this.zipCode = address.getZipcode();
            this.city = address.getCity();
            this.country = address.getCountry();
        }
    }


    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public String getZipCode() { return zipCode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getAdminLevel1() { return adminLevel1; }
    public String getAdminLevel2() { return adminLevel2; }
    public String getAdminLevel3() { return adminLevel3; }
    public String getAdminLevel4() { return adminLevel4; }

    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setAdminLevel1(String name) { this.adminLevel1 = name; }
    public void setAdminLevel2(String name) { this.adminLevel2 = name; }
    public void setAdminLevel3(String name) { this.adminLevel3 = name; }
    public void setAdminLevel4(String name) { this.adminLevel4 = name; }
}
