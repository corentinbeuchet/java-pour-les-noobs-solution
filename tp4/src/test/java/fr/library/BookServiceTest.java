package fr.library;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookServiceTest {

    private final BookService bookService = new BookService();

    @Test
    void shouldReturnBooksSortedByRequestedField() {
        Book effectiveJava = bookService.add(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        Book cleanCode = bookService.add(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));

        assertEquals(List.of(cleanCode, effectiveJava), bookService.findAll(SortField.TITLE));
        assertEquals(List.of(effectiveJava, cleanCode), bookService.findAll(SortField.AUTHOR));
        assertEquals(List.of(cleanCode, effectiveJava), bookService.findAll(SortField.ISBN));
    }

    @Test
    void deletingAnUnknownBookShouldFail() {
        assertThrows(BookNotFoundException.class, () -> bookService.delete("9780000000000"));
    }
}
