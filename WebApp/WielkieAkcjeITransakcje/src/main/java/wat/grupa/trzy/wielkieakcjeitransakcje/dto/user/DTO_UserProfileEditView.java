package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DTO_UserProfileEditView {
    private String email;
    private String username;
    private String accountType;

    private DTO_PersonalData personalDetails;
    private DTO_BusinessData businessDetails;

    private DTO_Address_Edit_User_Data mainAddress;
    private List<DTO_Address_Edit_User_Data> deliveryAddresses;

    public DTO_UserProfileEditView(UserData user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.accountType = user.getAccountType().name();
        this.deliveryAddresses = new ArrayList<>();

        // --- 1. OSOBA FIZYCZNA ---
        if (user.getPersonalData() != null) {
            this.personalDetails = new DTO_PersonalData(user.getPersonalData());

            // Pobieramy adres z listy wewnątrz PersonalData
            if (user.getPersonalData().getAddresses() != null && !user.getPersonalData().getAddresses().isEmpty()) {

                var addrEntity = user.getPersonalData().getAddresses().get(0);
                this.mainAddress = new DTO_Address_Edit_User_Data(addrEntity);
            }
        }

        // --- 2. FIRMA ---
        if (user.getBusinessData() != null) {
            this.businessDetails = new DTO_BusinessData(user.getBusinessData());

            // Pobieramy adres HQ bezpośrednio z BusinessData
            if (user.getBusinessData().getHqAddress() != null) {
                this.mainAddress = new DTO_Address_Edit_User_Data(user.getBusinessData().getHqAddress());
            }

            // Pobieramy adresy dostawy z BusinessData
            if (user.getBusinessData().getDeliveryAddresses() != null) {
                this.deliveryAddresses = user.getBusinessData().getDeliveryAddresses().stream()
                        .map(DTO_Address_Edit_User_Data::new)
                        .collect(Collectors.toList());
            }
        }
    }

    // Gettery
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getAccountType() { return accountType; }
    public DTO_PersonalData getPersonalDetails() { return personalDetails; }
    public DTO_BusinessData getBusinessDetails() { return businessDetails; }
    public DTO_Address_Edit_User_Data getMainAddress() { return mainAddress; }
    public List<DTO_Address_Edit_User_Data> getDeliveryAddresses() { return deliveryAddresses; }
}