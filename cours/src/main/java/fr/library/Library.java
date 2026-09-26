package fr.library;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Library {

    // L'ISBN est la clé : l'unicité est garantie par la Map, et la recherche par ISBN est directe.
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
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException("Livre introuvable : " + isbn);
        }
        return book;
    }

    public Book findBookByTitle(String title) {
        for (Book book : books.values()) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        throw new BookNotFoundException("Livre introuvable : " + title);
    }

    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthor().equals(author)) {
                result.add(book);
            }
        }
        return result;
    }

    public boolean containsIsbn(String isbn) {
        return books.containsKey(isbn);
    }

    public int size() {
        return books.size();
    }

    public void displayBooks() {
        for (Book book : books.values()) {
            System.out.println(book);
        }
    }
}
