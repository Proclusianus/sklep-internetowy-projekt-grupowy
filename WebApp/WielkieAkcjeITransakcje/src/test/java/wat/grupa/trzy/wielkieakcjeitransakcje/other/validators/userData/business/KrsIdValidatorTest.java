package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KrsIdValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", " 1234567890 ", "0000123456"})
    @DisplayName("Powinien zaakceptować prawidłowy numer KRS")
    void shouldAcceptValidKrs(String krs) {
        assertDoesNotThrow(() -> KrsIdValidator.validate(krs));
    }

    @Test
    @DisplayName("Powinien zignorować (nie rzucać błędu) pusty lub null-owy numer KRS")
    void shouldIgnoreNullOrEmptyKrs() {
        assertDoesNotThrow(() -> KrsIdValidator.validate(null));
        assertDoesNotThrow(() -> KrsIdValidator.validate(""));
        assertDoesNotThrow(() -> KrsIdValidator.validate("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "12345678901", "abcdefghij", "123-456-78-903"})
    @DisplayName("Powinien odrzucić nieprawidłowy format numeru KRS")
    void shouldRejectInvalidKrsFormat(String krs) {
        assertThrows(IllegalArgumentException.class, () -> KrsIdValidator.validate(krs));
    }
}
