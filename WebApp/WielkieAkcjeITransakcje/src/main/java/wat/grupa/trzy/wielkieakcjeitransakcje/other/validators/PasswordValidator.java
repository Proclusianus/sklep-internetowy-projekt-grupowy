package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_DIGIT = Pattern.compile("\\d"); // \\d to to samo co [0-9]
    private static final Pattern HAS_SPECIAL_CHAR = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    public static void validate(String password, String confirmPassword) {
        if (password == null || password.trim().isEmpty()) { throw new IllegalArgumentException("Hasło nie może być puste."); }
        if (!password.equals(confirmPassword)) { throw new IllegalArgumentException("Hasła nie są zgodne."); }
        if (password.length() < 7) { throw new IllegalArgumentException("Hasło musi mieć co najmniej 7 znaków."); }
        if (!HAS_LOWERCASE.matcher(password).find()) { throw new IllegalArgumentException("Hasło musi zawierać co najmniej jedną małą literę."); }
        if (!HAS_UPPERCASE.matcher(password).find()) { throw new IllegalArgumentException("Hasło musi zawierać co najmniej jedną dużą literę."); }
        if (!HAS_DIGIT.matcher(password).find()) { throw new IllegalArgumentException("Hasło musi zawierać co najmniej jedną cyfrę."); }
        if (!HAS_SPECIAL_CHAR.matcher(password).find()) { throw new IllegalArgumentException("Hasło musi zawierać co najmniej jeden znak specjalny (np. !@#$%)."); }
    }
}
