package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferta")
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aukcja_id", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "licytujacy_id", nullable = false)
    private UserData licytujacy;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal kwota;

    @Column(name = "data_zlozenia", nullable = false)
    private LocalDateTime dataZlozenia;

    public Oferta() {
        this.dataZlozenia = LocalDateTime.now();
    }

    public Oferta(Auction auction, UserData licytujacy, BigDecimal kwota) {
        this.auction = auction;
        this.licytujacy = licytujacy;
        this.kwota = kwota;
        this.dataZlozenia = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Auction getAuction() { return auction; }
    public void setAuction(Auction auction) { this.auction = auction; }
    public UserData getLicytujacy() { return licytujacy; }
    public void setLicytujacy(UserData licytujacy) { this.licytujacy = licytujacy; }
    public BigDecimal getKwota() { return kwota; }
    public void setKwota(BigDecimal kwota) { this.kwota = kwota; }
    public LocalDateTime getDataZlozenia() { return dataZlozenia; }
    public void setDataZlozenia(LocalDateTime dataZlozenia) { this.dataZlozenia = dataZlozenia; }
}