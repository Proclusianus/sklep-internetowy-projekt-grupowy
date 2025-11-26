package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rachunek")
public class Rachunek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserData user;

    private LocalDateTime dataWystawienia;

    @Column(precision = 10, scale = 2)
    private BigDecimal kwotaCalkowita;

    // TODO: Dodać konstruktory, gettery i settery
}
