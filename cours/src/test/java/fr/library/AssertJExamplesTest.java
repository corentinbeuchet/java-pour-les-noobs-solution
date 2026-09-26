package fr.library;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertJExamplesTest {

    @Test
    void shouldFindAllBooksOfAnAuthor() {
        Library library = new Library();
        library.addBook(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));
        library.addBook(new Book("9780134494166", "Clean Architecture", "Robert C. Martin", 2017));
        library.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));

        assertThat(library.findByAuthor("Robert C. Martin"))
                .hasSize(2)
                .extracting(Book::getTitle)
                .containsExactly("Clean Code", "Clean Architecture");
    }

    @Test
    void shouldExplainWhyABookIsMissing() {
        Library library = new Library();

        assertThatThrownBy(() -> library.findByIsbn("9780000000000"))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("9780000000000");
    }
}
