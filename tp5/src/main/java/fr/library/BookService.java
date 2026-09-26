package fr.library;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(SortField sort) {
        Sort order = switch (sort) {
            case TITLE -> Sort.by("title");
            case AUTHOR -> Sort.by("author", "title");
            case ISBN -> Sort.by("isbn");
        };
        return bookRepository.findAll(order);
    }

    @Transactional
    public Book add(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new DuplicateBookException("Livre déjà présent : " + book.getIsbn());
        }
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Livre introuvable : " + isbn));
        bookRepository.delete(book);
    }
}
