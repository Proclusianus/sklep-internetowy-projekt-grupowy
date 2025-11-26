package wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teryt_powiats")
public class TerytPowiat {

    @Id
    @Column(name = "code", length = 4)
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "powiat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TerytGmina> gimnas = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voivodeship_code")
    private TerytVoivodeship voivodeship;

    public TerytPowiat() {}

    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }
    public List<TerytGmina> getGimnas() { return gimnas; }
    public TerytVoivodeship getVoivodeship() { return voivodeship; }

    // Setters
    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
    public void setGimnas(List<TerytGmina> gimnas) { this.gimnas = gimnas; }
    public void setVoivodeship(TerytVoivodeship voivodeship) { this.voivodeship = voivodeship; }
}
