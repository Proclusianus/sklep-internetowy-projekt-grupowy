package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import java.time.LocalDate;
import java.util.Map;

public class UserSessionData {
    private String username;
    private String passwordHash;
    private String email;
    private Integer currentSessionID;
    private Integer userID;
    private E_TYP_KONTA accountType;
    private Map<LocalDate, Integer> sessionLogInTimes;
    private Map<LocalDate, Integer> lastActivityTimeForSession;
    private PersonalData personalData;
    private BusinessData businessData;
    private AdminData adminData;

    public UserSessionData(String username, String passwordHash, String email, Integer currentSessionID, Integer userID, E_TYP_KONTA accountType, Map<LocalDate, Integer> sessionLogInTimes, Map<LocalDate, Integer> lastActivityTimeForSession, PersonalData personalData, BusinessData businessData, AdminData adminData) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.currentSessionID = currentSessionID;
        this.userID = userID;
        this.accountType = accountType;
        this.sessionLogInTimes = sessionLogInTimes;
        this.lastActivityTimeForSession = lastActivityTimeForSession;
        this.personalData = personalData;
        this.businessData = businessData;
        this.adminData = adminData;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public Integer getCurrentSessionID() { return currentSessionID; }
    public Integer getUserID() { return userID; }
    public E_TYP_KONTA getAccountType() { return accountType; }
    public Map<LocalDate, Integer> getSessionLogInTimes() { return sessionLogInTimes; }
    public Map<LocalDate, Integer> getLastActivityTimeForSession() { return lastActivityTimeForSession; }
    public PersonalData getPersonalData() { return personalData; }
    public BusinessData getBusinessData() { return businessData; }
    public AdminData getAdminData() { return adminData; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEmail(String email) { this.email = email; }
    public void setCurrentSessionID(Integer currentSessionID) { this.currentSessionID = currentSessionID; }
    public void setUserID(Integer userID) { this.userID = userID; }
    public void setAccountType(E_TYP_KONTA accountType) { this.accountType = accountType; }
    public void setSessionLogInTimes(Map<LocalDate, Integer> sessionLogInTimes) { this.sessionLogInTimes = sessionLogInTimes; }
    public void setLastActivityTimeForSession(Map<LocalDate, Integer> lastActivityTimeForSession) { this.lastActivityTimeForSession = lastActivityTimeForSession; }
    public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }
    public void setBusinessData(BusinessData businessData) { this.businessData = businessData; }
    public void setAdminData(AdminData adminData) { this.adminData = adminData; }
}
