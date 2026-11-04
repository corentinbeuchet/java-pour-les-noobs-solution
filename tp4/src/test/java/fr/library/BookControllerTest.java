package fr.library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private static final String CLEAN_CODE_JSON = """
            {"isbn": "9780132350884", "title": "Clean Code", "author": "Robert C. Martin", "year": 2008}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private final Book cleanCode = new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008);

    @Test
    // Le service est un mock : ce test vérifie que le controller transmet le bon tri au service
    // et construit le JSON, pas que la liste est triée (c'est le rôle des tests de Library et de BookApiIT au TP 5).
    void getBooksShouldPassTheRequestedSortToTheService() throws Exception {
        given(bookService.findAll(SortField.AUTHOR)).willReturn(List.of(cleanCode));

        mockMvc.perform(get("/books").param("sort", "author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("9780132350884"))
                .andExpect(jsonPath("$[0].title").value("Clean Code"));

        verify(bookService).findAll(SortField.AUTHOR);
    }

    @Test
    void getBooksShouldSortByTitleByDefault() throws Exception {
        given(bookService.findAll(SortField.TITLE)).willReturn(List.of());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());

        verify(bookService).findAll(SortField.TITLE);
    }

    @Test
    void getBooksWithUnknownSortShouldReturn400() throws Exception {
        mockMvc.perform(get("/books").param("sort", "inconnu"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }

    @Test
    void postBookShouldReturn201() throws Exception {
        given(bookService.add(any(Book.class))).willReturn(cleanCode);

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(CLEAN_CODE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("9780132350884"));
    }

    @Test
    void postDuplicateBookShouldReturn409() throws Exception {
        given(bookService.add(any(Book.class))).willThrow(new DuplicateBookException("Livre déjà présent"));

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(CLEAN_CODE_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void postBookWithoutIsbnShouldReturn400() throws Exception {
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Sans ISBN", "author": "Anonyme", "year": 2020}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBookShouldReturn204() throws Exception {
        mockMvc.perform(delete("/books/9780132350884"))
                .andExpect(status().isNoContent());

        verify(bookService).delete("9780132350884");
    }

    @Test
    void deleteUnknownBookShouldReturn404() throws Exception {
        willThrow(new BookNotFoundException("Livre introuvable : 9780000000000"))
                .given(bookService).delete("9780000000000");

        mockMvc.perform(delete("/books/9780000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Livre introuvable : 9780000000000"));
    }
}
