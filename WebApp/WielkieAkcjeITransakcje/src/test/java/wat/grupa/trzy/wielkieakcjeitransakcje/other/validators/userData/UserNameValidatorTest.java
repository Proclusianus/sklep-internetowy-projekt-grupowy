package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserNameValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"jan.kowalski", "user_123", "Admin-Test", "test"})
    @DisplayName("Powinien zaakceptować prawidłową nazwę użytkownika")
    void shouldAcceptValidUsername(String username) {
        assertDoesNotThrow(() -> UserNameValidator.validate(username));
    }

    @Test
    @DisplayName("Powinien odrzucić pustą lub null-ową nazwę użytkownika")
    void shouldRejectNullOrEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> UserNameValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> UserNameValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> UserNameValidator.validate("   "));
    }

    @Test
    @DisplayName("Powinien odrzucić zbyt długą nazwę użytkownika")
    void shouldRejectTooLongUsername() {
        String longUsername = "a".repeat(41);
        assertThrows(IllegalArgumentException.class, () -> UserNameValidator.validate(longUsername));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user name", "user@test", "użytkownik", "test!"})
    @DisplayName("Powinien odrzucić nazwę użytkownika z nieprawidłowymi znakami")
    void shouldRejectInvalidCharactersInUsername(String username) {
        assertThrows(IllegalArgumentException.class, () -> UserNameValidator.validate(username));
    }
}
