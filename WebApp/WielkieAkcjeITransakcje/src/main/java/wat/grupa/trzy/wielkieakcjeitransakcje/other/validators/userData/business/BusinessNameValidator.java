package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business;

import java.util.regex.Pattern;

public class BusinessNameValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 200;
    private static final Pattern BUSINESS_NAME_PATTERN = Pattern.compile("^[\\p{L}\\p{N}.,&'()\\- /]+$");

    public static void validate(String businessName) {
        if (businessName == null || businessName.trim().isEmpty()) { throw new IllegalArgumentException("Name firmy nie może być pusta."); }
        if (businessName.length() < MIN_LENGTH || businessName.length() > MAX_LENGTH) { throw new IllegalArgumentException("Name firmy musi zawierać od 2 do 200 znaków."); }
        if (!businessName.matches(".*[\\p{L}\\p{N}].*")) { throw new IllegalArgumentException("Name firmy nie może się składać tylko ze spacji lub znaków specjalnych."); }
        if (!BUSINESS_NAME_PATTERN.matcher(businessName).matches()) { throw new IllegalArgumentException("Proszę podać prawidłową nazwę firmy. Może ona zawierać litery, cyfry i znaki specjalne, np. 'Acme Sp. z o.o.', 'J. Kowalski & Synowie'."); }
    }
}
