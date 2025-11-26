package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "cena_wywolawcza", precision = 10, scale = 2)
    private BigDecimal cenaWywolawcza;

    @Column(name = "aktualna_cena", precision = 10, scale = 2)
    private BigDecimal aktualnaCena;

    @Column(name = "data_zakonczenia")
    private LocalDateTime dataZakonczenia;

    @Column(name = "aktywna")
    private boolean aktywna = true;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("dataZlozenia DESC")
    private List<Oferta> oferty = new ArrayList<>();

    public Auction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public BigDecimal getCenaWywolawcza() { return cenaWywolawcza; }
    public void setCenaWywolawcza(BigDecimal cenaWywolawcza) { this.cenaWywolawcza = cenaWywolawcza; }
    public BigDecimal getAktualnaCena() { return aktualnaCena; }
    public void setAktualnaCena(BigDecimal aktualnaCena) { this.aktualnaCena = aktualnaCena; }
    public LocalDateTime getDataZakonczenia() { return dataZakonczenia; }
    public void setDataZakonczenia(LocalDateTime dataZakonczenia) { this.dataZakonczenia = dataZakonczenia; }
    public boolean isAktywna() { return aktywna; }
    public void setAktywna(boolean aktywna) { this.aktywna = aktywna; }
    public List<Oferta> getOferty() { return oferty; }
    public void setOferty(List<Oferta> oferty) { this.oferty = oferty; }
}