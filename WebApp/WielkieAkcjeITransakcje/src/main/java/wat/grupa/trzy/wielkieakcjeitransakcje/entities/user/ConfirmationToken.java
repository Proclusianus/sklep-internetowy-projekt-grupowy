package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    private OffsetDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_data_id")
    private UserData userData;

    public ConfirmationToken() {}
    public ConfirmationToken(String token, OffsetDateTime createdAt, OffsetDateTime expiresAt, UserData userData) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userData = userData;
    }

    // Getters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public OffsetDateTime getConfirmedAt() { return confirmedAt; }
    public UserData getUserData() { return userData; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setConfirmedAt(OffsetDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    public void setUserData(UserData userData) { this.userData = userData; }
}