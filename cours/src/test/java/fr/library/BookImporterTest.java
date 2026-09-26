package fr.library;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookImporterTest {

    private final IsbnLookup lookup = mock(IsbnLookup.class);
    private final Library library = new Library();
    private final BookImporter importer = new BookImporter(lookup, library);

    @Test
    void shouldImportBookWithTitleFromLookup() {
        when(lookup.titleOf("9780132350884")).thenReturn(Optional.of("Clean Code"));

        Book book = importer.importBook("9780132350884", "Robert C. Martin", 2008);

        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(library.containsIsbn("9780132350884")).isTrue();
        verify(lookup).titleOf("9780132350884");
    }

    @Test
    void shouldFailWhenIsbnIsUnknown() {
        when(lookup.titleOf("9780000000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> importer.importBook("9780000000000", "Anonyme", 2020))
                .isInstanceOf(BookNotFoundException.class);
        assertThat(library.size()).isZero();
    }
}
