package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;

@Entity
@Table(name = "sessions")
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session", unique = true, nullable = false)
    private String session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserData userData;

    public Sessions() {}

    // Getters
    public Long getId() {
        return id;
    }

    public String getSession() {
        return this.session;
    }

    public UserData getUserData() {
        return userData;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}