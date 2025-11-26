package wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce;

import java.math.BigDecimal;

public class DTO_AddProduct {
    private String name;
    private BigDecimal cena;
    private String description;
    private String kategoria;

    private String typSprzedazy;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getCena() { return cena; }
    public void setCena(BigDecimal cena) { this.cena = cena; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getKategoria() { return kategoria; }
    public void setKategoria(String kategoria) { this.kategoria = kategoria; }

    public String getTypSprzedazy() { return typSprzedazy; }
    public void setTypSprzedazy(String typSprzedazy) { this.typSprzedazy = typSprzedazy; }
}