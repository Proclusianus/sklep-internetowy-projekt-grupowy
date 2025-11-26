package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PhoneNumberValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"+48123456789", "+12125550123", "+442071234567"})
    @DisplayName("Powinien zaakceptować prawidłowy numer telefonu w formacie E.164")
    void shouldAcceptValidPhoneNumber(String number) {
        assertDoesNotThrow(() -> PhoneNumberValidator.validate(number));
    }

    @Test
    @DisplayName("Powinien odrzucić pusty lub null-owy numer telefonu")
    void shouldRejectNullOrEmptyPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> PhoneNumberValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> PhoneNumberValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> PhoneNumberValidator.validate("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",          // brak '+'
            "+48 123 456 789",    // spacja
            "48123456789",        // brak '+'
            "+12345678",          // za krótki
            "+1234567890123456"  // za długi
    })
    @DisplayName("Powinien odrzucić nieprawidłowy format numeru telefonu")
    void shouldRejectInvalidPhoneNumberFormat(String number) {
        assertThrows(IllegalArgumentException.class, () -> PhoneNumberValidator.validate(number));
    }
}
