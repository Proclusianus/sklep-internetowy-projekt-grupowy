package wat.grupa.trzy.wielkieakcjeitransakcje.enums;

public enum E_TYP_KONTA {
    OSOBA_FIZYCZNA("Osoba Fizyczna"),
    FIRMA("Osoba Prawna"),
    ADMIN("Administrator")
    ;

    private final String description;

    E_TYP_KONTA(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
