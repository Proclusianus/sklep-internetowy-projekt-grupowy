package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.payment;

public class CardValidator {
    private static final String CARD_REGEX = "^\\d{13,19}$";

    public static void validate(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) { throw new IllegalArgumentException("Karta nie została podana."); }
        String cleanedCardNumber = cardNumber.replaceAll("[\\s-]", "");
        if (!cleanedCardNumber.matches(CARD_REGEX)) { throw new IllegalArgumentException("Nieprawidłowy format numeru karty (wymagane 13-19 cyfr)."); }
    }
}
