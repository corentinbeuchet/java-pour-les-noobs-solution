package fr.library;

public final class IsbnValidator {

    private IsbnValidator() {
    }

    // Écrit en TDD : chaque ligne existe parce qu'un test l'a exigée.
    public static boolean isValid(String isbn) {
        if (isbn == null || !isbn.matches("\\d{13}")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        return sum % 10 == 0;
    }
}
