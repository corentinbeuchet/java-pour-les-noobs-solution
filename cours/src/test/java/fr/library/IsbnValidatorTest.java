package fr.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Les tests dans l'ordre où ils ont été écrits en TDD.
class IsbnValidatorTest {

    @Test
    void acceptsAValidIsbn13() {                      // 1. rouge : la classe n'existe pas
        assertTrue(IsbnValidator.isValid("9780132350884"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"978013235088", "97801323508845", "978-0132350884"})
    void rejectsWrongFormat(String isbn) {            // 2. rouge : isValid renvoyait toujours true
        assertFalse(IsbnValidator.isValid(isbn));
    }

    @Test
    void rejectsWrongCheckDigit() {                   // 3. rouge : on ne vérifiait que le format
        assertFalse(IsbnValidator.isValid("9780132350885"));
    }
}
