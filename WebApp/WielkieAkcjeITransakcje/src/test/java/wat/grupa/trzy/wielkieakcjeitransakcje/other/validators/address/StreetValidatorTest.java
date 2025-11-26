package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreetValidatorTest {

    @Test
    @DisplayName("Powinien zaakceptować prawidłową nazwę ulicy")
    void shouldAcceptValidStreetName() {
        assertDoesNotThrow(() -> StreetValidator.validate("Kwiatowa"));
        assertDoesNotThrow(() -> StreetValidator.validate("Aleje Jerozolimskie"));
        assertDoesNotThrow(() -> StreetValidator.validate("ul. 3 Maja"));
        assertDoesNotThrow(() -> StreetValidator.validate("Saint-Exupéry'ego"));
        assertDoesNotThrow(() -> StreetValidator.validate("Droga-Mleczna/3"));
    }

    @Test
    @DisplayName("Powinien odrzucić pustą lub null-ową nazwę ulicy")
    void shouldRejectNullOrEmptyStreetName() {
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "a12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"})
    @DisplayName("Powinien odrzucić nazwę ulicy o nieprawidłowej długości")
    void shouldRejectStreetNameWithInvalidLength(String input) {
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test--Test", "Test..Test", "Test//Test"})
    @DisplayName("Powinien odrzucić powtarzające się znaki specjalne w nazwie ulicy")
    void shouldRejectRepeatingSpecialCharactersInStreetName(String input) {
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate(input));
    }

    @Test
    @DisplayName("Powinien odrzucić nazwę ulicy składającą się tylko z cyfr")
    void shouldRejectStreetNameWithOnlyDigits() {
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate("12345"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ulica#", "Ulica$", "Ulica@"})
    @DisplayName("Powinien odrzucić nieprawidłowe znaki w nazwie ulicy")
    void shouldRejectInvalidCharactersInStreetName(String input) {
        assertThrows(IllegalArgumentException.class, () -> StreetValidator.validate(input));
    }
}
