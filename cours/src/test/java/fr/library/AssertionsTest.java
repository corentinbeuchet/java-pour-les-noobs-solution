package fr.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssertionsTest {

    private Library library;
    private Book book;

    @BeforeEach
    void setUp() {
        library = new Library();
        book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);
        library.addBook(book);
    }

    @Test
    void essentialAssertions() {
        assertEquals(1, library.size());
        assertTrue(library.containsIsbn("9780132350884"));
        assertFalse(library.containsIsbn("9780134685991"));
        assertNotNull(library.findByIsbn("9780132350884"));
    }

    @Test
    void shouldRejectDuplicateIsbn() {
        Book sameIsbn = new Book("9780132350884", "Autre titre", "Autre auteur", 2020);

        DuplicateBookException exception = assertThrows(DuplicateBookException.class,
                () -> library.addBook(sameIsbn));

        assertEquals("Livre déjà présent : 9780132350884", exception.getMessage());
    }

    @Test
    void shouldFindBookByIsbn() {
        Book found = library.findByIsbn("9780132350884");

        assertAll(
                () -> assertEquals("Clean Code", found.getTitle()),
                () -> assertEquals("Robert C. Martin", found.getAuthor()),
                () -> assertEquals(2008, found.getYear())
        );
    }

    @Test
    void shouldThrowWhenBookIsMissing() {
        assertThrows(BookNotFoundException.class, () -> library.findByIsbn("9780000000000"));
    }
}
