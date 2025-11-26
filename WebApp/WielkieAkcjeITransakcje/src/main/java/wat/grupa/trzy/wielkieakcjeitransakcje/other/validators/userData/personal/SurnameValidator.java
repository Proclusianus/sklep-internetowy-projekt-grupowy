package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.personal;

import java.util.regex.Pattern;

public class SurnameValidator {
    private static final int MAX_LENGTH = 100;
    private static final Pattern LAST_NAME_PATTERN = Pattern.compile("^\\p{L}+([\\s'\\-]\\p{L}+)*$");

    public static void validate(String surName) {
        if (surName == null || surName.isBlank()) { throw new IllegalArgumentException("Nazwisko nie może być puste."); }
        if (surName.length() > MAX_LENGTH) { throw new IllegalArgumentException("Nazwisko może zawierać tylko do 100 znaków."); }
        if (!LAST_NAME_PATTERN.matcher(surName).matches()) { throw new IllegalArgumentException("Nieprawidłowy format nazwiska. " +
                "Dozwolone są tylko litery oraz pojedyncze myślniki, apostrofy lub spacje oddzielające człony (np. 'Nowak-Kowalska', 'O'Malley', 'von der Leyen')"); }
    }
}
