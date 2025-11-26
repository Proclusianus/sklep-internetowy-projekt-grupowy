package wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt;

public class DTO_Teryt {
    private String code;
    private String name;

    public DTO_Teryt() {}
    public DTO_Teryt(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }

    // Setters
    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name; }
}
