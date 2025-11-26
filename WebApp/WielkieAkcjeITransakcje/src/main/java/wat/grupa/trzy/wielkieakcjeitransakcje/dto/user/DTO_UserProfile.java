package wat.grupa.trzy.wielkieakcjeitransakcje.dto.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;

public class DTO_UserProfile {
    private final Long id;
    private final String email;
    private final String username;
    private final String accountType;
    private final DTO_PersonalData personalData;
    private final DTO_BusinessData businessData;

    public DTO_UserProfile(UserData user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.accountType = user.getAccountType().name();
        this.personalData = (user.getPersonalData() != null) ? new DTO_PersonalData(user.getPersonalData()) : null;
        this.businessData = (user.getBusinessData() != null) ? new DTO_BusinessData(user.getBusinessData()) : null;
    }

    // Gettery
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getAccountType() { return accountType; }
    public DTO_PersonalData getPersonalData() { return personalData; }
    public DTO_BusinessData getBusinessData() { return businessData; }
}