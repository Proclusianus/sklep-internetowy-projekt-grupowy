package wat.grupa.trzy.wielkieakcjeitransakcje.enums;

public enum CONTACT_PERSON_TYPE {
    ADMIN("Dane admina"),
    FIRMA("Dane osoby kontaktowej firmy"),
    ;

    private final String description;

    CONTACT_PERSON_TYPE(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
