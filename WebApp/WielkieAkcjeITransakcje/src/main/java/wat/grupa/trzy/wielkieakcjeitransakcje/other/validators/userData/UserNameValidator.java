package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData;

import java.util.regex.Pattern;

public class UserNameValidator {
    private static final int MAX_LENGTH = 40;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    public static void validate(String username) {
        if (username == null || username.trim().isEmpty()) { throw new IllegalArgumentException("Name użytkownika nie może być pusta."); }
        if (username.length() > MAX_LENGTH) { throw new IllegalArgumentException("Name użytkownika może zawierać tylko do 40 znaków."); }
        if (!USERNAME_PATTERN.matcher(username).matches()) { throw new IllegalArgumentException("Nieprawidłowa name użytkownika. Dozwolone są litery, cyfry oraz znaki '.', '_', '-'."); }
    }
}
