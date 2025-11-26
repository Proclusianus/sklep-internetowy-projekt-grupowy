package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import java.util.regex.Pattern;

public class LocaleNumberValidator {
    private static final int MAX_LENGTH = 15;
    private static final Pattern LOCALE_NUMBER_PATTERN = Pattern.compile("^\\d+[A-Za-z]?([/-]\\d+[A-Za-z]?)?$");

    public static void validate(String localeNumber) {
        if (localeNumber == null || localeNumber.isBlank()) { throw new IllegalArgumentException("Numer budynku nie może być pusty."); }
        if (localeNumber.length() > MAX_LENGTH) { throw new IllegalArgumentException("Numer budynku może zawierać tylko do 15 znaków."); }
        if (!LOCALE_NUMBER_PATTERN.matcher(localeNumber).matches()) { throw new IllegalArgumentException("Nieprawidłowy numer budynku. Numer musi zaczynać się od cyfry i może zawierać litery oraz opcjonalnie jeden myślnik lub ukośnik, np. '12', '12A', '12/3'."); }
    }
}
