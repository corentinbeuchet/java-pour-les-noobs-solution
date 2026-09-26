package fr.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void shouldRejectInvalidIsbn(String isbn) {
        assertThrows(IllegalArgumentException.class,
                () -> new Book(isbn, "Clean Code", "Robert C. Martin", 2008));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void shouldRejectInvalidTitle(String title) {
        assertThrows(IllegalArgumentException.class,
                () -> new Book("9780132350884", title, "Robert C. Martin", 2008));
    }

    @Test
    void booksWithSameIsbnShouldBeEqual() {
        Book first = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);
        Book sameIsbn = new Book("9780132350884", "Clean Code (2e tirage)", "Robert C. Martin", 2009);

        assertEquals(first, sameIsbn);
        assertEquals(first.hashCode(), sameIsbn.hashCode());
    }

    @Test
    void booksWithDifferentIsbnShouldNotBeEqual() {
        Book cleanCode = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);
        Book effectiveJava = new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018);

        assertNotEquals(cleanCode, effectiveJava);
    }
}
