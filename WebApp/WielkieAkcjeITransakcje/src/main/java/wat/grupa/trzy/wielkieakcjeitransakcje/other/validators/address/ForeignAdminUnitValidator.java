package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import java.util.regex.Pattern;

public class ForeignAdminUnitValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    private static final Pattern FOREIGN_ADMIN_UNIT_PATTERN = Pattern.compile("^[\\p{L}\\p{N}.'-]+(\\s[\\p{L}\\p{N}.'-]+)*$");

    public static void validate(String admUnit) {
        if (admUnit == null || admUnit.isBlank()) { throw new IllegalArgumentException("Name jednostki administracyjnej nie może być pusta."); }
        if (admUnit.length() < MIN_LENGTH || admUnit.length() > MAX_LENGTH) { throw new IllegalArgumentException("Name jednostki administracyjnej musi zawierać od 2 do 100 znaków."); }
        if (admUnit.contains("--") || admUnit.contains("//") || admUnit.contains("..")) { throw new IllegalArgumentException("Nie mogą występować powtarzające się znaki (tj. '..','//','--')"); }
        if (!admUnit.matches(".*\\p{L}.*")) { throw new IllegalArgumentException("Name jednostki administracyjnej nie może się składać jedynie ze znaków specjalnych."); }
        if (!FOREIGN_ADMIN_UNIT_PATTERN.matcher(admUnit).matches()) { throw new IllegalArgumentException("Nieprawidłowa name jednostki administracyjnej. Dozwolone są litery, cyfry oraz znaki specjalne takie jak spacja, myślnik, apostrof i kropka."); }
    }
}
