package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NipValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"1111111111", "111-111-11-11", "111 111 11 11"})
    @DisplayName("Powinien zaakceptować prawidłowy NIP z poprawną sumą kontrolną")
    void shouldAcceptValidNip(String nip) {
        assertDoesNotThrow(() -> NipValidator.validate(nip));
    }

    @Test
    @DisplayName("Powinien odrzucić pusty lub null-owy NIP")
    void shouldRejectNullOrEmptyNip() {
        assertThrows(IllegalArgumentException.class, () -> NipValidator.validate(null));
        assertThrows(IllegalArgumentException.class, () -> NipValidator.validate(""));
        assertThrows(IllegalArgumentException.class, () -> NipValidator.validate("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "12345678901", "abcdefghij"})
    @DisplayName("Powinien odrzucić NIP o nieprawidłowym formacie")
    void shouldRejectNipWithInvalidFormat(String nip) {
        assertThrows(IllegalArgumentException.class, () -> NipValidator.validate(nip));
    }

    @Test
    @DisplayName("Powinien odrzucić NIP z nieprawidłową sumą kontrolną")
    void shouldRejectNipWithInvalidChecksum() {
        assertThrows(IllegalArgumentException.class, () -> NipValidator.validate("1111111112"));
    }
}
