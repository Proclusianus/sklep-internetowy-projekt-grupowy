package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user_data")
public class UserData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private E_TYP_KONTA accountType;

    @Column(name = "enabled")
    private boolean enabled = false;

    // Relacje
    @OneToMany(mappedBy = "userData", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sessions> userSessionsIDs;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "personal_data_id")
    private PersonalData personalData;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "business_data_id")
    private BusinessData businessData;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "admin_data_id")
    private AdminData adminData;

    @OneToMany(mappedBy = "userData", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfirmationToken> confirmationTokens;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public UserData() {}

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public E_TYP_KONTA getAccountType() { return accountType; }
    public PersonalData getPersonalData() { return personalData; }
    public BusinessData getBusinessData() { return businessData; }
    public AdminData getAdminData() { return adminData; }
    public List<Sessions> getUserSessionsIDs() { return userSessionsIDs; }
    public boolean isEnabled() { return enabled; }
    public List<ConfirmationToken> getConfirmationTokens() { return confirmationTokens; }
    public Wallet getWallet() { return wallet; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setAccountType(E_TYP_KONTA accountType) { this.accountType = accountType; }
    public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }
    public void setBusinessData(BusinessData businessData) { this.businessData = businessData; }
    public void setAdminData(AdminData adminData) { this.adminData = adminData; }
    public void setUserSessionsIDs(List<Sessions> userSessionsIDs) { this.userSessionsIDs = userSessionsIDs; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setConfirmationTokens(List<ConfirmationToken> confirmationTokens) { this.confirmationTokens = confirmationTokens; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
}