package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocaleNumberValidatorTest {

    @Test
    @DisplayName("Powinien zaakceptować prawidłowy numer lokalu")
    void shouldAcceptValidLocaleNumber() {
        assertDoesNotThrow(() -> LocaleNumberValidator.validate("1"));
        assertDoesNotThrow(() -> LocaleNumberValidator.validate("12"));
        assertDoesNotThrow(() -> LocaleNumberValidator.validate("12A"));
        assertDoesNotThrow(() -> LocaleNumberValidator.validate("12/3"));
        assertDoesNotThrow(() -> LocaleNumberValidator.validate("12A/3B"));
        assertDoesNotThrow(() -> LocaleNumberValidator.validate("12-3"));
    }

    @Test
    @DisplayName("Powinien odrzucić pusty lub null-owy numer lokalu")
    void shouldRejectNullOrEmptyLocaleNumber() {
        assertThrows(IllegalArgumentException.class, () -> LocaleNumberValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleNumberValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleNumberValidator.validate("  "));
    }

    @Test
    @DisplayName("Powinien odrzucić numer lokalu o nieprawidłowej długości")
    void shouldRejectLocaleNumberWithInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> LocaleNumberValidator.validate("1234567890123456"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A12", "12//3", "12--3", "12/A/3", "12 3", "12-3/4"})
    @DisplayName("Powinien odrzucić nieprawidłowy format numeru lokalu")
    void shouldRejectInvalidLocaleNumberFormat(String input) {
        assertThrows(IllegalArgumentException.class, () -> LocaleNumberValidator.validate(input));
    }
}
