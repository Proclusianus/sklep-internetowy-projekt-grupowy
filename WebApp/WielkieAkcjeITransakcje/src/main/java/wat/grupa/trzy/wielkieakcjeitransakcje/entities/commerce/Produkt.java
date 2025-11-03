package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produkt")
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nazwa;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cena;

    @Lob
    private String opis;

    // TODO: Dodać konstruktory, gettery i settery
}
