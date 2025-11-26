package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import java.util.regex.Pattern;

public class FirstNameValidator {
    private static final int MAX_LENGTH = 60;
    private static final Pattern FIRST_NAME_PATTERN = Pattern.compile("^\\p{L}+(\\s\\p{L}+)?$");

    public static void validate(String firstName) {
        if (firstName == null || firstName.isBlank()) { throw new IllegalArgumentException("Imię nie może być puste."); }
        if (firstName.length() > MAX_LENGTH) { throw new IllegalArgumentException("Imię może zawierać tylko do 60 znaków."); }
        if (!FIRST_NAME_PATTERN.matcher(firstName).matches()) { throw new IllegalArgumentException("Imię musi składać się wyłącznie z liter. W przypadku podwójnych imion, należy użyć pojedynczej spacji (np. 'Anna Maria')"); }
    }
}
