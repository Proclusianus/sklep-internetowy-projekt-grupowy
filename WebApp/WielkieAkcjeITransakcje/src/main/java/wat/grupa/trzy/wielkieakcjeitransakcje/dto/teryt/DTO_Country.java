package wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt;

public class DTO_Country {
    private String namePl;
    private String codeIsoAlpha2;

    public DTO_Country() {}
    public DTO_Country(String namePl, String codeIsoAlpha2) {
        this.namePl = namePl;
        this.codeIsoAlpha2 = codeIsoAlpha2;
    }

    // Gettery
    public String getNamePl() { return namePl; }
    public String getCodeIsoAlpha2() { return codeIsoAlpha2; }

    // Settery
    public void setNamePl(String namePl) { this.namePl = namePl; }
    public void setCodeIsoAlpha2(String codeIsoAlpha2) { this.codeIsoAlpha2 = codeIsoAlpha2; }
}
