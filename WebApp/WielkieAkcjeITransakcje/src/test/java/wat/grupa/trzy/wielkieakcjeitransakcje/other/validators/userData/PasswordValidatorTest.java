package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordValidatorTest {

    @Test
    @DisplayName("Powinien zaakceptować prawidłowe i zgodne hasło")
    void shouldAcceptValidPassword() {
        assertDoesNotThrow(() -> PasswordValidator.validate("ValidPass1!", "ValidPass1!"));
    }

    @Test
    @DisplayName("Powinien odrzucić puste lub null-owe hasło")
    void shouldRejectNullOrEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate(null, null));
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("", ""));
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("  ", "  "));
    }

    @Test
    @DisplayName("Powinien odrzucić niezgodne hasła")
    void shouldRejectMismatchedPasswords() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("ValidPass1!", "DifferentPass1!"));
    }

    @Test
    @DisplayName("Powinien odrzucić hasło krótsze niż 7 znaków")
    void shouldRejectPasswordShorterThan7Chars() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("Vp1!", "Vp1!"));
    }

    @Test
    @DisplayName("Powinien odrzucić hasło bez małej litery")
    void shouldRejectPasswordWithoutLowercase() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("VALIDPASS1!", "VALIDPASS1!"));
    }

    @Test
    @DisplayName("Powinien odrzucić hasło bez dużej litery")
    void shouldRejectPasswordWithoutUppercase() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("validpass1!", "validpass1!"));
    }

    @Test
    @DisplayName("Powinien odrzucić hasło bez cyfry")
    void shouldRejectPasswordWithoutDigit() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("ValidPass!", "ValidPass!"));
    }

    @Test
    @DisplayName("Powinien odrzucić hasło bez znaku specjalnego")
    void shouldRejectPasswordWithoutSpecialChar() {
        assertThrows(IllegalArgumentException.class, () -> PasswordValidator.validate("ValidPass1", "ValidPass1"));
    }
}
