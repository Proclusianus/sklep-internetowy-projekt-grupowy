package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import java.util.regex.Pattern;

public class ForeignCityValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    private static final Pattern FOREIGN_CITY_PATTERN = Pattern.compile("^[\\p{L}.'-]+(\\s[\\p{L}.'-]+)*$");

    public static void validate(String cityName) {
        if (cityName == null || cityName.isBlank()) { throw new IllegalArgumentException("Name miasta nie może być pusta."); }
        if (cityName.length() < MIN_LENGTH || cityName.length() > MAX_LENGTH) { throw new IllegalArgumentException("Name miasta musi zawierać od 2 do 100 znaków."); }
        if (cityName.contains("--") || cityName.contains("//") || cityName.contains("..")) { throw new IllegalArgumentException("Nie mogą występować powtarzające się znaki (tj. '..','//','--')"); }
        if (!cityName.matches(".*\\p{L}.*")) { throw new IllegalArgumentException("Name miasta nie może się składać jedynie ze znaków specjalnych."); }
        if (!FOREIGN_CITY_PATTERN.matcher(cityName).matches()) { throw new IllegalArgumentException("Nieprawidłowa name miejscowości. Dozwolone są wyłącznie litery oraz znaki specjalne takie jak spacja, myślnik, apostrof i kropka."); }
    }
}
