package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import java.util.List;

public class DTO_UserUpdateForm {
    private String email;
    private String username;
    private String accountType;

    private String currentPassword;
    private String newPassword;

    private DTO_PersonalData personalDetails;
    private DTO_BusinessData businessDetails;
    private DTO_Address_Edit_User_Data mainAddress;
    private List<DTO_Address_Edit_User_Data> deliveryAddresses;

    public DTO_UserUpdateForm() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getCurrentPassword() { return currentPassword; } // Getter
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; } // Setter

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public DTO_PersonalData getPersonalDetails() { return personalDetails; }
    public void setPersonalDetails(DTO_PersonalData personalDetails) { this.personalDetails = personalDetails; }
    public DTO_BusinessData getBusinessDetails() { return businessDetails; }
    public void setBusinessDetails(DTO_BusinessData businessDetails) { this.businessDetails = businessDetails; }
    public DTO_Address_Edit_User_Data getMainAddress() { return mainAddress; }
    public void setMainAddress(DTO_Address_Edit_User_Data mainAddress) { this.mainAddress = mainAddress; }
    public List<DTO_Address_Edit_User_Data> getDeliveryAddresses() { return deliveryAddresses; }
    public void setDeliveryAddresses(List<DTO_Address_Edit_User_Data> deliveryAddresses) { this.deliveryAddresses = deliveryAddresses; }
}