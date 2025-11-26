package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.PersonalData;
import java.util.List;
import java.util.stream.Collectors;

public class DTO_PersonalData {
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public DTO_PersonalData() {}

    public DTO_PersonalData(PersonalData data) {
        this.firstName = data.getName();
        this.lastName = data.getSurname();
        this.phoneNumber = data.getPhoneNumber();
    }

    // GETTERY
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }

    // SETTERY
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}