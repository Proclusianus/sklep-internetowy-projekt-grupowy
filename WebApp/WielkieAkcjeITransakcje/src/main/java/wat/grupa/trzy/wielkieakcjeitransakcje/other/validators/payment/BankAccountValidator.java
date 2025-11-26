package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.payment;

public class BankAccountValidator {
    private static final String IBAN_REGEX = "^[A-Z]{2}\\d{2}[A-Z0-9]{11,30}$";

    public static void validate(String iban) {
        if (iban == null || iban.isBlank()) { throw new IllegalArgumentException("Numer konta nie został wypełniony."); }
        String cleanedIban = iban.replaceAll("[\\s-]", "").toUpperCase();
        if (!cleanedIban.matches(IBAN_REGEX)) { throw new IllegalArgumentException("Nieprawidłowy format numeru konta bankowego."); }
    }
}