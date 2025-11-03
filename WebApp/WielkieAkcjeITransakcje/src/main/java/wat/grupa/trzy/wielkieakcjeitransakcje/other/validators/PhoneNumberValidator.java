package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators;

import java.util.regex.Pattern;

public class PhoneNumberValidator {
    private static final Pattern E164_PATTERN = Pattern.compile("^\\+\\d{9,15}$");

    public static void validate(String n) {
        if (n == null || n.trim().isEmpty()) { throw new IllegalArgumentException("Numer telefonu nie może być pusty."); }
        if (!E164_PATTERN.matcher(n).matches()) { throw new IllegalArgumentException("Numer telefonu jest w nieprawidłowym formacie"); }
    }
}
