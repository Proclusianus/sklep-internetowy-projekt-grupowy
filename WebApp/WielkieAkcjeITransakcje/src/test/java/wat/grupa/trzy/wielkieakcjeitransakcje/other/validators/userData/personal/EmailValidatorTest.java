package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "test@example.com",
            "test.name@example.co.uk",
            "test+alias@domain.org",
            "user123@sub.domain.io"
    })
    @DisplayName("Powinien zaakceptować prawidłowy adres email")
    void shouldAcceptValidEmail(String email) {
        assertDoesNotThrow(() -> EmailValidator.validate(email));
    }

    @Test
    @DisplayName("Powinien odrzucić pusty lub null-owy email")
    void shouldRejectNullOrEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> EmailValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> EmailValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> EmailValidator.validate("  "));
    }

    @Test
    @DisplayName("Powinien odrzucić zbyt długi adres email")
    void shouldRejectTooLongEmail() {
        String longEmail = "a".repeat(245) + "@example.com"; // 245 + 1 + 11 = 257
        assertThrows(IllegalArgumentException.class, () -> EmailValidator.validate(longEmail));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@example.com",
            "Joe Smith <email@example.com>",
            "email.example.com",
            "email@example@example.com",
            "email@example..com"
    })
    @DisplayName("Powinien odrzucić nieprawidłowy format adresu email")
    void shouldRejectInvalidEmailFormat(String email) {
        assertThrows(IllegalArgumentException.class, () -> EmailValidator.validate(email));
    }
}
