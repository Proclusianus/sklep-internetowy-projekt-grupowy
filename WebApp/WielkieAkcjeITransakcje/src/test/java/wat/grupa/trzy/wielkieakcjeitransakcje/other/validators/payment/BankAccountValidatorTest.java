package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountValidatorTest {

    @ParameterizedTest(name = "Poprawny IBAN: {0}")
    @ValueSource(strings = {
            "PL12123456780000000000000000", // Standardowy PL
            "PL 12 1234 5678 0000 0000 0000 0000", // Spacje
            "PL-12-1234-5678-0000-0000-0000-0000", // Myślniki
            "DE89370400440532013000",              // Zagraniczny (Niemcy)
            "pl12123456780000000000000000" // Małe litery (kod powinien zamienić na UpperCase)
    })
    void shouldValidateCorrectIban(String iban) {
        assertDoesNotThrow(() -> BankAccountValidator.validate(iban));
    }

    @ParameterizedTest(name = "Błędny format IBAN: {0}")
    @ValueSource(strings = {
            "1234567890",                  // Brak kodu kraju
            "P112345678",                  // Zły kod kraju (cyfra zamiast litery)
            "PL12",                        // Za krótki
            "PL121234567800000000000000001234567890", // Za długi (>34 znaki)
            "PL12!@#$%"                    // Znaki specjalne
    })
    void shouldThrowExceptionForInvalidFormat(String iban) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            BankAccountValidator.validate(iban);
        });
        assertEquals("Nieprawidłowy format numeru konta bankowego.", exception.getMessage());
    }

    @ParameterizedTest(name = "Pusty input: {0}")
    @NullSource
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionForEmptyInput(String iban) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            BankAccountValidator.validate(iban);
        });
        assertEquals("Numer konta nie został wypełniony.", exception.getMessage());
    }
}