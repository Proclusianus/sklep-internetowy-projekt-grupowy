package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import java.util.regex.Pattern;

public class ApartmentNumberValidator {
    private static final int MAX_LENGTH = 15;
    private static final Pattern APARTMENT_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9]+([/\\-][A-Za-z0-9]+)?$");

    public static void validate(String apartmentNumber) {
        if (apartmentNumber == null || apartmentNumber.trim().isEmpty()) { return; }
        if (apartmentNumber.length() > MAX_LENGTH) { throw new IllegalArgumentException("Numer mieszkania może zawierać tylko do 15 znaków."); }
        if (!APARTMENT_NUMBER_PATTERN.matcher(apartmentNumber).matches()) { throw new IllegalArgumentException("Nieprawidłowy numer mieszkania. Dozwolone są tylko litery i cyfry. Opcjonalnie można użyć jednego myślnika lub ukośnika."); }
    }
}
