package fr.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibrarySearchTest {

    private static final Book CLEAN_CODE = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);
    private static final Book CLEAN_ARCHITECTURE = new Book("9780134494166", "Clean Architecture", "Robert C. Martin", 2017);
    private static final Book EFFECTIVE_JAVA = new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018);
    private static final Book DESIGN_PATTERNS = new Book("9780201633610", "Design Patterns", "Erich Gamma", 1994);

    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
        library.addBook(EFFECTIVE_JAVA);
        library.addBook(CLEAN_CODE);
        library.addBook(DESIGN_PATTERNS);
        library.addBook(CLEAN_ARCHITECTURE);
    }

    @Test
    void shouldSortByTitle() {
        assertEquals(List.of(CLEAN_ARCHITECTURE, CLEAN_CODE, DESIGN_PATTERNS, EFFECTIVE_JAVA),
                library.booksSortedByTitle());
    }

    @Test
    void shouldSortByAuthorThenTitle() {
        assertEquals(List.of(DESIGN_PATTERNS, EFFECTIVE_JAVA, CLEAN_ARCHITECTURE, CLEAN_CODE),
                library.booksSortedByAuthor());
    }

    @Test
    void shouldSortByIsbn() {
        assertEquals(List.of(CLEAN_CODE, CLEAN_ARCHITECTURE, EFFECTIVE_JAVA, DESIGN_PATTERNS),
                library.booksSortedByIsbn());
    }

    @Test
    void sortingAnEmptyLibraryShouldReturnAnEmptyList() {
        assertTrue(new Library().booksSortedByTitle().isEmpty());
    }

    @Test
    void booksWithSameTitleShouldBothBeKept() {
        Library withDuplicateTitles = new Library();
        Book firstEdition = new Book("9780201485677", "Refactoring", "Martin Fowler", 1999);
        Book secondEdition = new Book("9780134757599", "Refactoring", "Martin Fowler", 2018);
        withDuplicateTitles.addBook(firstEdition);
        withDuplicateTitles.addBook(secondEdition);

        assertEquals(2, withDuplicateTitles.booksSortedByTitle().size());
    }

    static Stream<Arguments> searches() {
        return Stream.of(
                Arguments.of(new ByTitle("clean code"), List.of(CLEAN_CODE)),
                Arguments.of(new ByAuthor("Robert C. Martin"), List.of(CLEAN_CODE, CLEAN_ARCHITECTURE)),
                Arguments.of(new ByYearRange(2010, 2020), List.of(EFFECTIVE_JAVA, CLEAN_ARCHITECTURE)),
                Arguments.of(new ByAuthor("Inconnu"), List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("searches")
    void shouldSearchByCriteria(SearchCriteria criteria, List<Book> expected) {
        assertEquals(expected, library.search(criteria));
    }

    @Test
    void shouldGroupBooksByAuthor() {
        Map<String, List<Book>> byAuthor = library.booksByAuthor();

        assertEquals(List.of(CLEAN_CODE, CLEAN_ARCHITECTURE), byAuthor.get("Robert C. Martin"));
        assertEquals(3, byAuthor.size());
    }

    @Test
    void optionalShouldBeEmptyWhenIsbnIsUnknown() {
        assertTrue(library.findOptionalByIsbn("9780000000000").isEmpty());
    }
}
