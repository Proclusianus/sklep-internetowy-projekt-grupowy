package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "aukcja")
public class Aukcja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produkt_id", nullable = false)
    private Produkt produkt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprzedajacy_user_id", nullable = false)
    private UserData sprzedajacy;

    @Column(precision = 10, scale = 2)
    private BigDecimal cenaWywolawcza;

    private LocalDateTime dataZakonczenia;

    // TODO: Dodać konstruktory, gettery i settery
}
