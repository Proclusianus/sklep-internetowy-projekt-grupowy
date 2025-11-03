package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;

@Entity
@Table(name = "opinie")
public class Opinie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produkt_id", nullable = false)
    private Produkt produkt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_user_id", nullable = false)
    private UserData autor;

    private Integer ocena;

    @Lob
    private String tresc;

    // TODO: Dodać konstruktory, gettery i settery
}