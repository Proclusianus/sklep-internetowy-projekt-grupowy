package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_data")
public class UserData {

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

    public UserData() {}

    // Getters
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public E_TYP_KONTA getAccountType() { return accountType; }
    public PersonalData getPersonalData() { return personalData; }
    public BusinessData getBusinessData() { return businessData; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEmail(String email) { this.email = email; }
    public void setAccountType(E_TYP_KONTA accountType) { this.accountType = accountType; }
    public void setPersonalData(PersonalData personalData) { this.personalData = personalData; }
    public void setBusinessData(BusinessData businessData) { this.businessData = businessData; }
}

