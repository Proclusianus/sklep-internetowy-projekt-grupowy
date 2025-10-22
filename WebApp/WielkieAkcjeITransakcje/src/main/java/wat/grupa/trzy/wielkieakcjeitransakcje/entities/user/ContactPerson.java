package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

public class ContactPerson {
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    public ContactPerson(String n, String sn, String e, String pn) {
        this.name = n;
        this.surname = sn;
        this.email = e;
        this.phoneNumber = pn;
    }

    // Getters
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
