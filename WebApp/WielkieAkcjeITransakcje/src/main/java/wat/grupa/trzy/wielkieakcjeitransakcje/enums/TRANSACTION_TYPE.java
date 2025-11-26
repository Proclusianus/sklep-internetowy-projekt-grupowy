package wat.grupa.trzy.wielkieakcjeitransakcje.enums;

public enum TRANSACTION_TYPE {
    PURCHASE("Zakup"),
    SALE("Sprzedaż"),
    TOP_UP_GIFT_CARD("Realizacja karty podarunkowej"),
    TOP_UP_CARD("Doładowanie kartą"),
    TOP_UP_BANK_ACC("Doładowanie numerem konta bankowego"),
    MANUAL_CORRECTION("Poprawka systemowa")
    ;

    private final String description;

    TRANSACTION_TYPE(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
