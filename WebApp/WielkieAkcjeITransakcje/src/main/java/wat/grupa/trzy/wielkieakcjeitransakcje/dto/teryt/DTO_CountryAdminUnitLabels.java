package wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt;

public class DTO_CountryAdminUnitLabels {
    private String level1;
    private String level2;
    private String level3;
    private String level4;

    public DTO_CountryAdminUnitLabels() {}
    public DTO_CountryAdminUnitLabels(String level1, String level2, String level3, String level4) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.level4 = level4;
    }

    // Getters
    public String getLevel1() { return level1; }
    public String getLevel2() { return level2; }
    public String getLevel3() { return level3; }
    public String getLevel4() { return level4; }

    // Setters
    public void setLevel1(String level1) { this.level1 = level1; }
    public void setLevel2(String level2) { this.level2 = level2; }
    public void setLevel3(String level3) { this.level3 = level3; }
    public void setLevel4(String level4) { this.level4 = level4; }
}
