package wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt;

import jakarta.persistence.*;

@Entity
@Table(name = "countries")
public class Countries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_pl", nullable = false, unique = true)
    private String namePl;

    @Column(name = "code_iso_alpha2", nullable = false, unique = true, length = 2)
    private String codeIsoAlpha2;

    public Countries() {}

    // Gettery
    public Integer getId() { return id; }
    public String getNamePl() { return namePl; }
    public String getCodeIsoAlpha2() { return codeIsoAlpha2; }

    // Settery
    public void setId(Integer id) { this.id = id; }
    public void setNamePl(String namePl) { this.namePl = namePl; }
    public void setCodeIsoAlpha2(String codeIsoAlpha2) { this.codeIsoAlpha2 = codeIsoAlpha2; }
}
