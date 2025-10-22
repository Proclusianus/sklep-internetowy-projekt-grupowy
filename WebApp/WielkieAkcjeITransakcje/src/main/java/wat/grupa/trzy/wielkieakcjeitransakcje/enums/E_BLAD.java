package wat.grupa.trzy.wielkieakcjeitransakcje.enums;

public enum E_BLAD {
    NIEZNANY("Nieznany błąd"),
    ;

    private final String err_msg;
    private E_BLAD(String err_msg) {
        this.err_msg = err_msg;
    }
    public String getErrMsg() {
        return err_msg;
    }
}
