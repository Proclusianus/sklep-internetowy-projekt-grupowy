package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ZipcodeValidatorTest {

    @Test
    @DisplayName("Powinien zaakceptować prawidłowy kod pocztowy")
    void shouldAcceptValidZipcode() {
        assertDoesNotThrow(() -> ZipcodeValidator.validate("00-950"));
        assertDoesNotThrow(() -> ZipcodeValidator.validate("12345"));
        assertDoesNotThrow(() -> ZipcodeValidator.validate("SW1A 0AA"));
        assertDoesNotThrow(() -> ZipcodeValidator.validate("10115"));
        assertDoesNotThrow(() -> ZipcodeValidator.validate("K1A 0B1"));
    }

    @Test
    @DisplayName("Powinien odrzucić pusty lub null-owy kod pocztowy")
    void shouldRejectNullOrEmptyZipcode() {
        assertThrows(IllegalArgumentException.class, () -> ZipcodeValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> ZipcodeValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> ZipcodeValidator.validate("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "1234567890123"})
    @DisplayName("Powinien odrzucić kod pocztowy o nieprawidłowej długości")
    void shouldRejectZipcodeWithInvalidLength(String input) {
        assertThrows(IllegalArgumentException.class, () -> ZipcodeValidator.validate(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"00_950", "00--950", "00 950 ", " 00-950"})
    @DisplayName("Powinien odrzucić nieprawidłowy format kodu pocztowego")
    void shouldRejectInvalidZipcodeFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> ZipcodeValidator.validate(input));
    }
}
