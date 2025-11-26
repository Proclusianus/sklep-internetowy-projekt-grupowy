package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final int MAX_LENGTH = 254;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public static void validate(String email) {
        if (email == null || email.trim().isEmpty()) { throw new IllegalArgumentException("Email nie może być pusty."); }
        if (email.length() > MAX_LENGTH) { throw new IllegalArgumentException("Name konta email może zawierać tylko do 254 znaków."); }
        if (!EMAIL_PATTERN.matcher(email).matches()) { throw new IllegalArgumentException("Proszę podać prawidłowy adres e-mail."); }
    }
}
