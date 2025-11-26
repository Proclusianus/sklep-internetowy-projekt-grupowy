package wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teryt_voivodeships")
public class TerytVoivodeship {

    @Id
    @Column(name = "code", length = 2)
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "voivodeship", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TerytPowiat> powiats = new ArrayList<>();

    public TerytVoivodeship() {}

    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }
    public List<TerytPowiat> getPowiats() { return powiats; }

    // Setters
    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setPowiats(List<TerytPowiat> powiats) { this.powiats = powiats; }
}
