package wat.grupa.trzy.wielkieakcjeitransakcje.enums;

public enum ADDRESS_TYPE {
    ADRES_DOMOWY("Adres domowy"),
    ADRES_FIRMY("Adres firmy"),
    ADRESY_DOSTAWCZE_FIRMY("Adresy dostawcze firmy")
    ;

    private final String description;

    ADDRESS_TYPE(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
