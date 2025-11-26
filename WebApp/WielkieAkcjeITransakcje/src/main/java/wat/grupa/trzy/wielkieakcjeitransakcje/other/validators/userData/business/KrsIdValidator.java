package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business;

import java.util.regex.Pattern;

public class KrsIdValidator {
    private static final Pattern KRSID_PATTERN = Pattern.compile("^\\d{10}$");

    public static void validate(String krs) {
        if (krs == null || krs.trim().isEmpty()) { return; } // Dla pustego pomiń
        if (!KRSID_PATTERN.matcher(krs.replaceAll("\\D", "")).matches()) { throw new IllegalArgumentException("Nieprawidłowy format numeru KRS. Numer musi składać się z dokładnie 10 cyfr."); }
    }
}
