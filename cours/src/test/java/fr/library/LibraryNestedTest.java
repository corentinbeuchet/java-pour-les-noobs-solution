package fr.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Une bibliothèque")
class LibraryNestedTest {

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Nested
    @DisplayName("quand elle est vide")
    class WhenEmpty {

        @Test
        @DisplayName("ne contient aucun livre")
        void hasNoBook() {
            assertEquals(0, library.size());
        }

        @Test
        @DisplayName("refuse de supprimer un livre")
        void cannotRemove() {
            assertThrows(BookNotFoundException.class, () -> library.removeBook("9780132350884"));
        }
    }

    @Nested
    @DisplayName("quand elle contient Clean Code")
    class WithOneBook {

        @BeforeEach
        void addBook() {
            library.addBook(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));
        }

        @Test
        @DisplayName("refuse un second livre avec le même ISBN")
        void rejectsDuplicate() {
            Book duplicate = new Book("9780132350884", "Autre", "Autre", 2020);
            assertThrows(DuplicateBookException.class, () -> library.addBook(duplicate));
        }
    }
}
