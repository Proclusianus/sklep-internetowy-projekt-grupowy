package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ForeignCityValidatorTest {

    @Test
    @DisplayName("Powinien zaakceptować prawidłową nazwę miasta")
    void shouldAcceptValidCityName() {
        assertDoesNotThrow(() -> ForeignCityValidator.validate("Warszawa"));
        assertDoesNotThrow(() -> ForeignCityValidator.validate("New York"));
        assertDoesNotThrow(() -> ForeignCityValidator.validate("Saint-Louis"));
        assertDoesNotThrow(() -> ForeignCityValidator.validate("O'Fallon"));
    }

    @Test
    @DisplayName("Powinien odrzucić pustą lub null-ową nazwę miasta")
    void shouldRejectNullOrEmptyCityName() {
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "a12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"})
    @DisplayName("Powinien odrzucić nazwę miasta o nieprawidłowej długości")
    void shouldRejectCityNameWithInvalidLength(String input) {
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test--Test", "Test..Test", "Test//Test"})
    @DisplayName("Powinien odrzucić powtarzające się znaki specjalne w nazwie miasta")
    void shouldRejectRepeatingSpecialCharactersInCityName(String input) {
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate(input));
    }

    @Test
    @DisplayName("Powinien odrzucić nazwę miasta składającą się tylko ze znaków specjalnych")
    void shouldRejectCityNameWithOnlySpecialCharacters() {
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate(".-'"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Miasto1", "Miasto@", "Miasto#", "Miasto$"})
    @DisplayName("Powinien odrzucić nieprawidłowe znaki w nazwie miasta")
    void shouldRejectInvalidCharactersInCityName(String input) {
        assertThrows(IllegalArgumentException.class, () -> ForeignCityValidator.validate(input));
    }
}
