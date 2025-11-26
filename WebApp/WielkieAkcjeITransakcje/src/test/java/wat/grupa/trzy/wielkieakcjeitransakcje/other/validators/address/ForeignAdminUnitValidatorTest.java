package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ForeignAdminUnitValidatorTest {

    @Test
    @DisplayName("Powinien zaakceptować prawidłową nazwę jednostki administracyjnej")
    void shouldAcceptValidAdminUnit() {
        assertDoesNotThrow(() -> ForeignAdminUnitValidator.validate("Mazowieckie"));
        assertDoesNotThrow(() -> ForeignAdminUnitValidator.validate("New York"));
        assertDoesNotThrow(() -> ForeignAdminUnitValidator.validate("Saint-Petersburg"));
        assertDoesNotThrow(() -> ForeignAdminUnitValidator.validate("O'Connell Street"));
        assertDoesNotThrow(() -> ForeignAdminUnitValidator.validate("1st District"));
    }

    @Test
    @DisplayName("Powinien odrzucić pustą lub null-ową nazwę jednostki administracyjnej")
    void shouldRejectNullOrEmptyAdminUnit() {
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "a12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"})
    @DisplayName("Powinien odrzucić nazwę jednostki administracyjnej o nieprawidłowej długości")
    void shouldRejectAdminUnitWithInvalidLength(String input) {
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test--Test", "Test..Test", "Test//Test"})
    @DisplayName("Powinien odrzucić powtarzające się znaki specjalne")
    void shouldRejectRepeatingSpecialCharacters(String input) {
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate(input));
    }

    @Test
    @DisplayName("Powinien odrzucić nazwę składającą się tylko ze znaków specjalnych")
    void shouldRejectAdminUnitWithOnlySpecialCharacters() {
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate(".-'"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Miasto@", "Miasto#", "Miasto$"})
    @DisplayName("Powinien odrzucić nieprawidłowe znaki")
    void shouldRejectInvalidCharacters(String input) {
        assertThrows(IllegalArgumentException.class, () -> ForeignAdminUnitValidator.validate(input));
    }
}
