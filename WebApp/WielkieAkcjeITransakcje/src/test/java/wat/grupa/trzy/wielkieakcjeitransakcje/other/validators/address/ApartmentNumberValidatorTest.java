package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentNumberValidatorTest {

    ////////////////////////////////////
    // --- TESTY POZYTYWNE/HAPPY PATH) ---
    ////////////////////////////////////
    @Test
    void validate_shouldPassForValidSimpleNumber() {
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate("123"));
    }

    @Test
    void validate_shouldPassForValidNumberWithLetter() {
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate("10A"));
    }

    @Test
    void validate_shouldPassForValidNumberWithSlash() {
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate("15/2"));
    }

    @Test
    void validate_shouldPassForValidNumberWithDash() {
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate("C-1"));
    }

    ////////////////////////////////////
    // --- TESTY PRZYPADKÓW BRZEGOWYCH/EDGE CASES ---
    ////////////////////////////////////
    @Test
    void validate_shouldPassForNullInput() {
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate(null));
    }

    @Test
    void validate_shouldPassForEmptyInput() {
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate(""));
        assertDoesNotThrow(() -> ApartmentNumberValidator.validate("   ")); // Sprawdzamy też same spacje
    }

    ////////////////////////////////////
    // --- TESTY NEGATYWNE/SAD PATH ---
    ////////////////////////////////////
    @Test
    void validate_shouldThrowExceptionWhenEndsWithSeparator() {
        assertThrows(IllegalArgumentException.class, () -> ApartmentNumberValidator.validate("12-"));
    }

    @Test
    void validate_shouldThrowExceptionWhenNumberIsTooLong() {
        String longNumber = "1234567890123456"; // 16 znaków
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ApartmentNumberValidator.validate(longNumber)
        );
        assertEquals("Numer mieszkania może zawierać tylko do 15 znaków.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12@A",      // Znak specjalny
            "12 5",      // Spacja
            "12,5",      // Przecinek
            "12.5",      // Kropka
            "12_A",      // Podkreślenie
            "ąćś"        // Polskie znaki
    })
    void validate_shouldThrowExceptionWhenContainsInvalidCharacters(String invalidInput) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ApartmentNumberValidator.validate(invalidInput)
        );
        assertTrue(exception.getMessage().contains("Nieprawidłowy numer mieszkania"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-12",       // Zaczyna się od myślnika
            "/12A",      // Zaczyna się od ukośnika
            "12-",       // Kończy się myślnikiem
            "12/",       // Kończy się ukośnikiem
            "12--5",     // Podwójny myślnik
            "12//5",     // Podwójny ukośnik
            "12/-5"      // Mieszane separatory
    })
    void validate_shouldThrowExceptionForInvalidSeparatorUsage(String invalidInput) {
        assertThrows(
                IllegalArgumentException.class,
                () -> ApartmentNumberValidator.validate(invalidInput)
        );
    }

    @Test
    void validate_shouldThrowExceptionWhenOnlySeparatorIsProvided() {
        assertThrows(IllegalArgumentException.class, () -> ApartmentNumberValidator.validate("-"));
        assertThrows(IllegalArgumentException.class, () -> ApartmentNumberValidator.validate("/"));
    }
}