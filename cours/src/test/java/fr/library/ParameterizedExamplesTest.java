package fr.library;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParameterizedExamplesTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void shouldRejectInvalidIsbn(String isbn) {
        assertThrows(IllegalArgumentException.class,
                () -> new Book(isbn, "Clean Code", "Robert C. Martin", 2008));
    }

    @ParameterizedTest
    @CsvSource({
            "Robert C. Martin, 2",
            "Joshua Bloch,     1",
            "Inconnu,          0"
    })
    void shouldCountBooksByAuthor(String author, int expected) {
        Library library = new Library();
        library.addBook(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));
        library.addBook(new Book("9780134494166", "Clean Architecture", "Robert C. Martin", 2017));
        library.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));

        assertEquals(expected, library.findByAuthor(author).size());
    }
}
