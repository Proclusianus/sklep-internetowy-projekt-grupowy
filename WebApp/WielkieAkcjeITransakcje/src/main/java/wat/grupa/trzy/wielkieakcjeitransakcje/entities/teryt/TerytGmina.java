package wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teryt_gminas")
public class TerytGmina {

    @Id
    @Column(name = "code", length = 7)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "powiat_code")
    private TerytPowiat powiat;

    @OneToMany(mappedBy = "gmina", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TerytLocality> localities = new ArrayList<>();

    public TerytGmina() {}

    // Getters
    public String getCode() { return code; }
    public TerytPowiat getPowiat() { return powiat; }
    public String getName() { return name; }
    public String getType() { return type; }
    public List<TerytLocality> getLocalities() { return localities; }

    // Setters
    public void setCode(String code) { this.code = code; }
    public void setPowiat(TerytPowiat powiat) { this.powiat = powiat; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setLocalities(List<TerytLocality> localities) { this.localities = localities; }
}
