package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurnameValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"Kowalski", "Nowak-Kowalska", "O'Malley", "von der Leyen", "Żółciński"})
    @DisplayName("Powinien zaakceptować prawidłowe nazwisko")
    void shouldAcceptValidSurname(String surname) {
        assertDoesNotThrow(() -> SurnameValidator.validate(surname));
    }

    @Test
    @DisplayName("Powinien odrzucić puste lub null-owe nazwisko")
    void shouldRejectNullOrEmptySurname() {
        assertThrows(IllegalArgumentException.class, () -> SurnameValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> SurnameValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> SurnameValidator.validate("   "));
    }

    @Test
    @DisplayName("Powinien odrzucić zbyt długie nazwisko")
    void shouldRejectTooLongSurname() {
        assertThrows(IllegalArgumentException.class, () -> SurnameValidator.validate("a".repeat(101)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Kowalski123",
            "Nowak--Kowalska",
            "O''Malley",
            "von  der Leyen",
            "-Nowak",
            "Kowalska-"
    })
    @DisplayName("Powinien odrzucić nazwisko z nieprawidłowymi znakami lub formatowaniem")
    void shouldRejectInvalidSurname(String surname) {
        assertThrows(IllegalArgumentException.class, () -> SurnameValidator.validate(surname));
    }
}
