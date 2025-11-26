package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.Address;

public class DTO_Address_Edit_User_Data {
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String zipCode;
    private String city;
    private String country;
    private String addressType; // MAIN lub DELIVERY

    public DTO_Address_Edit_User_Data() {}

    // --- TEN KONSTRUKTOR NAPRAWIA BŁĄD ---
    public DTO_Address_Edit_User_Data(Address address) {
        if (address != null) {
            this.street = address.getStreet();
            this.houseNumber = address.getHouseNumber();
            this.apartmentNumber = address.getApartmentNumber();
            this.zipCode = address.getZipcode(); // Zwróć uwagę na wielkość liter (Zipcode)
            this.city = address.getCity();
            this.country = address.getCountry();
            if (address.getAddressType() != null) {
                this.addressType = address.getAddressType().name();
            }
        }
    }
    // -------------------------------------

    // Gettery i Settery
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }

    // Gettery dla JS (admin level) - opcjonalnie, żeby nie było błędów jeśli JS o nie pyta
    public String getAdminLevel1() { return ""; }
    public String getAdminLevel2() { return ""; }
    public String getAdminLevel3() { return ""; }
    public String getAdminLevel4() { return ""; }
}