package wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teryt_localities")
public class TerytLocality {

    @Id
    @Column(name = "symbol", length = 7)
    private String symbol;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gmina_code")
    private TerytGmina gmina;

    @OneToMany(mappedBy = "locality", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TerytStreet> streets = new ArrayList<>();

    public TerytLocality() {}

    // Getters
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public TerytGmina getGmina() { return gmina; }
    public List<TerytStreet> getStreets() { return streets; }

    // Setters
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setName(String name) { this.name = name; }
    public void setGmina(TerytGmina gmina) { this.gmina = gmina; }
    public void setStreets(List<TerytStreet> streets) { this.streets = streets; }
}
