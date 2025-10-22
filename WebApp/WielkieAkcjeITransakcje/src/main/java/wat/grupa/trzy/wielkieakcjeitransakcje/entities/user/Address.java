package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

public class Address {
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String zipcode;
    private String city;
    private String country;

    public Address(String street, String houseNumber, String apartmentNumber, String zipcode, String city, String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }

    // Getters
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getApartmentNumber() { return apartmentNumber; }
    public String getZipcode() { return zipcode; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    // Setters
    public void setStreet(String street) { this.street = street; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
}
