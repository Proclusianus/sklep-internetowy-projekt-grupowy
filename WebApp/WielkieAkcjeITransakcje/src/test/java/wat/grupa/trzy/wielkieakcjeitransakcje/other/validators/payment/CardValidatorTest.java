package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardValidatorTest {

    @ParameterizedTest(name = "Poprawny numer karty: {0}")
    @ValueSource(strings = {
            "1234567812345678",         // Standardowe 16 cyfr
            "1234 5678 1234 5678",      // Ze spacjami
            "1234-5678-1234-5678",      // Z myślnikami
            "1234567890123",            // Minimum (13)
            "1234567890123456789"       // Maximum (19)
    })
    void shouldValidateCorrectCardNumbers(String cardNumber) {
        assertDoesNotThrow(() -> CardValidator.validate(cardNumber));
    }

    @ParameterizedTest(name = "Błędny format (za krótki/długi/litery): {0}")
    @ValueSource(strings = {
            "123456789012",             // Za krótki (12)
            "12345678901234567890",     // Za długi (20)
            "1234abcd12345678",         // Litery w środku
            "1234-5678-1234-aaaa",      // Litery na końcu
            "1234@#$%"                  // Znaki specjalne
    })
    void shouldThrowExceptionForInvalidFormat(String cardNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardValidator.validate(cardNumber);
        });
        assertEquals("Nieprawidłowy format numeru karty (wymagane 13-19 cyfr).", exception.getMessage());
    }

    @ParameterizedTest(name = "Pusty input: {0}")
    @NullSource                 // Testuje null
    @ValueSource(strings = {"", "   "}) // Testuje pusty i same spacje
    void shouldThrowExceptionForEmptyInput(String cardNumber) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CardValidator.validate(cardNumber);
        });
        assertEquals("Karta nie została podana.", exception.getMessage());
    }
}
