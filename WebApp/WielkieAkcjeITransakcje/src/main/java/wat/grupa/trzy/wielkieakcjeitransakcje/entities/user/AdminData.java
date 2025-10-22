package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

public class AdminData {
    private Integer adminID;
    private ContactPerson person;

    public AdminData(Integer adminID, ContactPerson person) {
        this.adminID = adminID;
        this.person = person;
    }

    // Getters
    public Integer getAdminID() { return adminID; }

    // Setters
    public void setAdminID(Integer adminID) { this.adminID = adminID; }
}
