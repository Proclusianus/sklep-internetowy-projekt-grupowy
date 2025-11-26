package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import java.util.regex.Pattern;

public class StreetValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    private static final Pattern STREETNAME_PATTERN = Pattern.compile("^[\\p{L}\\p{N}.'\\-/]+(\\s[\\p{L}\\p{N}.'\"\\-/]+)*$");

    public static void validate(String streetName) {
        if (streetName == null || streetName.isBlank()) { throw new IllegalArgumentException("Name ulicy nie może być pusta."); }
        if (streetName.length() < MIN_LENGTH || streetName.length() > MAX_LENGTH) { throw new IllegalArgumentException("Name ulicy musi zawierać od 2 do 100 znaków."); }
        if (streetName.contains("--") || streetName.contains("//") || streetName.contains("..")) { throw new IllegalArgumentException("Nie mogą występować powtarzające się znaki (tj. '..','//','--')"); }
        if (!streetName.matches(".*\\p{L}.*")) { throw new IllegalArgumentException("Name ulicy nie może się składać jedynie z cyfr."); }
        if (!STREETNAME_PATTERN.matcher(streetName).matches()) { throw new IllegalArgumentException("Nieprawidłowa name ulicy. Dozwolone są litery, cyfry oraz pojedyncze spacje, myślniki, apostrofy, cudzysłowy, kropki i ukośniki."); }
    }
}
