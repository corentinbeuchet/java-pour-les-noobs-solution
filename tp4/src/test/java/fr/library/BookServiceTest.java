package fr.library;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookServiceTest {

    private final BookService bookService = new BookService();

    @Test
    void shouldReturnBooksSortedByRequestedField() {
        // Ordre d'ajout différent des trois ordres attendus : sans tri, chaque assertion échoue
        Book designPatterns = bookService.add(new Book("9780201633610", "Design Patterns", "Erich Gamma", 1994));
        Book cleanCode = bookService.add(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));
        Book effectiveJava = bookService.add(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));

        assertEquals(List.of(cleanCode, designPatterns, effectiveJava), bookService.findAll(SortField.TITLE));
        assertEquals(List.of(designPatterns, effectiveJava, cleanCode), bookService.findAll(SortField.AUTHOR));
        assertEquals(List.of(cleanCode, effectiveJava, designPatterns), bookService.findAll(SortField.ISBN));
    }

    @Test
    void deletingAnUnknownBookShouldFail() {
        assertThrows(BookNotFoundException.class, () -> bookService.delete("9780000000000"));
    }
}
