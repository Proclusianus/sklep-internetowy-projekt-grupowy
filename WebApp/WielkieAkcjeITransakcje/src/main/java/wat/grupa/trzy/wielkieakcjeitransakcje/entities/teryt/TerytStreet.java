package wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt;

import jakarta.persistence.*;

@Entity
@Table(name = "teryt_streets")
public class TerytStreet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_type")
    private String streetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_symbol")
    private TerytLocality locality;

    public TerytStreet() {}

    // Getters
    public Long getId() { return id; }
    public String getStreetName() { return streetName; }
    public String getStreetType() { return streetType; }
    public TerytLocality getLocality() { return locality; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStreetName(String streetName) { this.streetName = streetName; }
    public void setStreetType(String streetType) { this.streetType = streetType; }
    public void setLocality(TerytLocality locality) { this.locality = locality; }
}
