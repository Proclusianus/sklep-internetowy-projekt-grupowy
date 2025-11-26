package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BusinessNameValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "Acme Corporation",
            "J. Kowalski & Synowie",
            "Firma (z o.o.)",
            "Tech-Solutions / Innovations",
            "123 Budownictwo Sp. z o.o."
    })
    @DisplayName("Powinien zaakceptować prawidłową nazwę firmy")
    void shouldAcceptValidBusinessName(String name) {
        assertDoesNotThrow(() -> BusinessNameValidator.validate(name));
    }

    @Test
    @DisplayName("Powinien odrzucić pustą lub null-ową nazwę firmy")
    void shouldRejectNullOrEmptyBusinessName() {
        assertThrows(IllegalArgumentException.class, () -> BusinessNameValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> BusinessNameValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> BusinessNameValidator.validate("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "A1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4I5J6K7L8M9N0O1P2Q3R4S5T6U7V8W9X0Y1Z2A1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4I5J6K7L8M9N0O1P2Q3R4S5T6U7V8W9X0Y1Z2"})
    @DisplayName("Powinien odrzucić nazwę firmy o nieprawidłowej długości")
    void shouldRejectBusinessNameWithInvalidLength(String name) {
        assertThrows(IllegalArgumentException.class, () -> BusinessNameValidator.validate(name));
    }

    @Test
    @DisplayName("Powinien odrzucić nazwę składającą się tylko ze znaków specjalnych")
    void shouldRejectBusinessNameWithOnlySpecialChars() {
        assertThrows(IllegalArgumentException.class, () -> BusinessNameValidator.validate(".,&'()- /"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Firma@Nielegalna", "Zakazany Znak #"})
    @DisplayName("Powinien odrzucić nieprawidłowe znaki w nazwie firmy")
    void shouldRejectInvalidCharactersInBusinessName(String name) {
        assertThrows(IllegalArgumentException.class, () -> BusinessNameValidator.validate(name));
    }
}
