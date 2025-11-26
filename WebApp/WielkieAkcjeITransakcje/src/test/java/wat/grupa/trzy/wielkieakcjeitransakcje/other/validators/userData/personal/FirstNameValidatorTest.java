package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FirstNameValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"Jan", "Anna Maria", "Zażółć", "Elżbieta"})
    @DisplayName("Powinien zaakceptować prawidłowe imię")
    void shouldAcceptValidFirstName(String name) {
        assertDoesNotThrow(() -> FirstNameValidator.validate(name));
    }

    @Test
    @DisplayName("Powinien odrzucić puste lub null-owe imię")
    void shouldRejectNullOrEmptyFirstName() {
        assertThrows(IllegalArgumentException.class, () -> FirstNameValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> FirstNameValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> FirstNameValidator.validate("  "));
    }

    @Test
    @DisplayName("Powinien odrzucić zbyt długie imię")
    void shouldRejectTooLongFirstName() {
        assertThrows(IllegalArgumentException.class, () -> FirstNameValidator.validate("a".repeat(61)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jan123", "Anna-Maria", "Jan.", "Anna  Maria"})
    @DisplayName("Powinien odrzucić imię z nieprawidłowymi znakami lub formatowaniem")
    void shouldRejectInvalidFirstName(String name) {
        assertThrows(IllegalArgumentException.class, () -> FirstNameValidator.validate(name));
    }
}
