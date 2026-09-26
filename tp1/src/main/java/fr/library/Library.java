package fr.library;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        if (containsIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Livre déjà présent : " + book.getIsbn());
        }
        books.add(book);
    }

    public void removeBook(String isbn) {
        books.remove(findByIsbn(isbn));
    }

    public Book findByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        throw new IllegalArgumentException("Livre introuvable : " + isbn);
    }

    public Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        throw new IllegalArgumentException("Livre introuvable : " + title);
    }

    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                result.add(book);
            }
        }
        return result;
    }

    public boolean containsIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return books.size();
    }

    public void displayBooks() {
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
