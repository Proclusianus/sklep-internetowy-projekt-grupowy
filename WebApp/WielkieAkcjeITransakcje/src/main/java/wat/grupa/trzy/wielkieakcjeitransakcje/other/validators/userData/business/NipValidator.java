package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.userData.business;

import java.util.regex.Pattern;

public class NipValidator {
    private static final Pattern NIP_PATTERN = Pattern.compile("\\d{10}");

    public static void validate(String nip) {
        if (nip == null || nip.trim().isEmpty()) { throw new IllegalArgumentException("Numer NIP nie może być pusty."); }

        String cleanedNip = nip.replaceAll("[\\s-]", "");
        if (!NIP_PATTERN.matcher(cleanedNip).matches()) { throw new IllegalArgumentException("Podany numer NIP jest nieprawidłowy. Upewnij się, że wpisałeś 10 cyfr (np. '123-456-78-90') i że numer nie zawiera literówek."); }

        int[] weights = {6, 5, 7, 2, 3, 4, 5, 6, 7};
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cleanedNip.charAt(i)) * weights[i];
        }
        int controlDigit = Character.getNumericValue(cleanedNip.charAt(9));
        if ((sum % 11) != controlDigit) { throw new IllegalArgumentException("Numer NIP nie posiada poprawnej sumy kontrolnej. Sprawdź czy wpisany numer NIP jest poprawny."); }
    }
}
