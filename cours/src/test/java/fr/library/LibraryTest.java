package fr.library;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryTest {

    @Test
    void shouldAddBook() {
        // Arrange
        Library library = new Library();
        Book book = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);

        // Act
        library.addBook(book);

        // Assert
        assertEquals(1, library.size());
        assertTrue(library.containsIsbn("9780132350884"));
    }
}
