package wat.grupa.trzy.wielkieakcjeitransakcje.other.validators.address;

import java.util.regex.Pattern;

public class ZipcodeValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 12;
    private static final Pattern UNIVERSAL_POSTAL_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9]+([ -][A-Za-z0-9]+)*$");

    public static void validate(String zipcode) {
        if (zipcode == null || zipcode.isBlank()) { throw new IllegalArgumentException("Kod pocztowy nie może być pusty."); }
        if (zipcode.length() < MIN_LENGTH || zipcode.length() > MAX_LENGTH) { throw new IllegalArgumentException("Kod pocztowy musi zawierać od 2 do 12 znaków."); }
        if (!UNIVERSAL_POSTAL_CODE_PATTERN.matcher(zipcode).matches()) { throw new IllegalArgumentException("Nieprawidłowy format kodu pocztowego. " +
                "Dozwolone są wyłącznie litery (bez polskich znaków), cyfry oraz pojedyncze myślniki i spacje jako separatory. Długość musi wynosić od 2 do 12 znaków."); }
    }
}
