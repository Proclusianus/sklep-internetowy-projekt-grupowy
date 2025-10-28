package wat.grupa.trzy.wielkieakcjeitransakcje.entities.user;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_data")
public class AdminData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_person_id")
    private ContactPerson contactPersonAdmin;

    public AdminData() {}
}
