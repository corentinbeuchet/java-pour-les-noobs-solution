package fr.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Test d'intégration de bout en bout : HTTP -> controller -> service -> JPA -> vraie base PostgreSQL
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
class BookApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.save(new Book("9780134685991", "Effective Java", "Joshua Bloch", 2018));
        bookRepository.save(new Book("9780132350884", "Clean Code", "Robert C. Martin", 2008));
    }

    @Test
    void getBooksShouldReturnBooksSortedByTitle() throws Exception {
        mockMvc.perform(get("/books").param("sort", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[1].title").value("Effective Java"));
    }

    @Test
    void postBookShouldSaveItInTheDatabase() throws Exception {
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"isbn": "9780201633610", "title": "Design Patterns", "author": "Erich Gamma", "year": 1994}
                                """))
                .andExpect(status().isCreated());

        assertTrue(bookRepository.existsByIsbn("9780201633610"));
    }

    @Test
    void postDuplicateBookShouldReturn409() throws Exception {
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"isbn": "9780132350884", "title": "Clean Code", "author": "Robert C. Martin", "year": 2008}
                                """))
                .andExpect(status().isConflict());

        assertEquals(2, bookRepository.count());
    }

    @Test
    void deleteBookShouldRemoveItFromTheDatabase() throws Exception {
        mockMvc.perform(delete("/books/9780132350884"))
                .andExpect(status().isNoContent());

        assertTrue(bookRepository.findByIsbn("9780132350884").isEmpty());
    }

    @Test
    void deleteUnknownBookShouldReturn404() throws Exception {
        mockMvc.perform(delete("/books/9780000000000"))
                .andExpect(status().isNotFound());
    }
}
