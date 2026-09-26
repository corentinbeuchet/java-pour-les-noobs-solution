package fr.library;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Library {

    private final Map<String, Book> books = new LinkedHashMap<>();

    public void addBook(Book book) {
        if (books.containsKey(book.getIsbn())) {
            throw new DuplicateBookException("Livre déjà présent : " + book.getIsbn());
        }
        books.put(book.getIsbn(), book);
    }

    public void removeBook(String isbn) {
        if (books.remove(isbn) == null) {
            throw new BookNotFoundException("Livre introuvable : " + isbn);
        }
    }

    public Book findByIsbn(String isbn) {
        return findOptionalByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException("Livre introuvable : " + isbn));
    }

    public Optional<Book> findOptionalByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    public Book findBookByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Livre introuvable : " + title));
    }

    public List<Book> findByAuthor(String author) {
        return search(new ByAuthor(author));
    }

    public List<Book> booksSortedByTitle() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .toList();
    }

    public List<Book> booksSortedByAuthor() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getAuthor).thenComparing(Book::getTitle))
                .toList();
    }

    public List<Book> booksSortedByIsbn() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getIsbn))
                .toList();
    }

    public List<Book> search(SearchCriteria criteria) {
        return books.values().stream()
                .filter(book -> switch (criteria) {
                    case ByTitle(String title) -> book.getTitle().equalsIgnoreCase(title);
                    case ByAuthor(String author) -> book.getAuthor().equals(author);
                    case ByYearRange(int from, int to) -> book.getYear() >= from && book.getYear() <= to;
                })
                .toList();
    }

    public Map<String, List<Book>> booksByAuthor() {
        return books.values().stream()
                .collect(Collectors.groupingBy(Book::getAuthor));
    }

    public boolean containsIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    public int size() {
        return books.size();
    }

    public void displayBooks() {
        books.values().forEach(System.out::println);
    }
}
