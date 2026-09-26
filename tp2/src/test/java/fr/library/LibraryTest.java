package fr.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryTest {

    private Library library;
    private Book cleanCode;
    private Book cleanArchitecture;

    @BeforeEach
    void setUp() {
        library = new Library();
        cleanCode = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);
        cleanArchitecture = new Book("9780134494166", "Clean Architecture", "Robert C. Martin", 2017);
    }

    @Test
    void shouldFindBookByTitle() {
        library.addBook(cleanCode);

        Book book = library.findBookByTitle("Clean Code");

        assertEquals("9780132350884", book.getIsbn());
    }

    @Test
    void newLibraryShouldBeEmpty() {
        assertEquals(0, library.size());
    }

    @Test
    void shouldAddBook() {
        library.addBook(cleanCode);

        assertEquals(1, library.size());
        assertTrue(library.containsIsbn("9780132350884"));
    }

    @Test
    void shouldRejectDuplicateIsbn() {
        library.addBook(cleanCode);
        Book sameIsbn = new Book("9780132350884", "Autre titre", "Autre auteur", 2020);

        assertThrows(DuplicateBookException.class, () -> library.addBook(sameIsbn));
        assertEquals(1, library.size());
    }

    @Test
    void shouldFindBookByIsbn() {
        library.addBook(cleanCode);

        assertEquals(cleanCode, library.findByIsbn("9780132350884"));
    }

    @Test
    void shouldThrowWhenIsbnIsUnknown() {
        assertThrows(BookNotFoundException.class, () -> library.findByIsbn("9780000000000"));
    }

    @Test
    void shouldThrowWhenTitleIsUnknown() {
        library.addBook(cleanCode);

        assertThrows(BookNotFoundException.class, () -> library.findBookByTitle("Effective Java"));
    }

    @Test
    void shouldFindAllBooksOfAnAuthor() {
        library.addBook(cleanCode);
        library.addBook(cleanArchitecture);
        library.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));

        List<Book> books = library.findByAuthor("Robert C. Martin");

        assertEquals(List.of(cleanCode, cleanArchitecture), books);
    }

    @Test
    void shouldReturnEmptyListWhenAuthorIsUnknown() {
        library.addBook(cleanCode);

        assertTrue(library.findByAuthor("Inconnu").isEmpty());
    }

    @Test
    void shouldRemoveBook() {
        library.addBook(cleanCode);

        library.removeBook("9780132350884");

        assertFalse(library.containsIsbn("9780132350884"));
    }

    @Test
    void shouldThrowWhenRemovingUnknownBook() {
        assertThrows(BookNotFoundException.class, () -> library.removeBook("9780000000000"));
    }

    @Test
    void exceptionsShouldShareTheSameParent() {
        assertTrue(LibraryException.class.isAssignableFrom(BookNotFoundException.class));
        assertTrue(LibraryException.class.isAssignableFrom(DuplicateBookException.class));
    }
}
