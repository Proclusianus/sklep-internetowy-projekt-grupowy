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

    @Column(name = "admin_area_level_1_label")
    private String adminAreaLevel1Label;

    @Column(name = "admin_area_level_2_label")
    private String adminAreaLevel2Label;

    @Column(name = "admin_area_level_3_label")
    private String adminAreaLevel3Label;

    @Column(name = "admin_area_level_4_label")
    private String adminAreaLevel4Label;

    public Countries() {}

    public Integer getId() { return id; }
    public String getNamePl() { return namePl; }
    public String getCodeIsoAlpha2() { return codeIsoAlpha2; }
    public String getAdminAreaLevel1Label() { return adminAreaLevel1Label; }
    public String getAdminAreaLevel2Label() { return adminAreaLevel2Label; }
    public String getAdminAreaLevel3Label() { return adminAreaLevel3Label; }
    public String getAdminAreaLevel4Label() { return adminAreaLevel4Label; }

    public void setId(Integer id) { this.id = id; }
    public void setNamePl(String namePl) { this.namePl = namePl; }
    public void setCodeIsoAlpha2(String codeIsoAlpha2) { this.codeIsoAlpha2 = codeIsoAlpha2; }
    public void setAdminAreaLevel1Label(String adminAreaLevel1Label) { this.adminAreaLevel1Label = adminAreaLevel1Label; }
    public void setAdminAreaLevel2Label(String adminAreaLevel2Label) { this.adminAreaLevel2Label = adminAreaLevel2Label; }
    public void setAdminAreaLevel3Label(String adminAreaLevel3Label) { this.adminAreaLevel3Label = adminAreaLevel3Label; }
    public void setAdminAreaLevel4Label(String adminAreaLevel4Label) { this.adminAreaLevel4Label = adminAreaLevel4Label; }
}
