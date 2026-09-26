package fr.library;

import java.util.Objects;

public class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final int year;

    public Book(String isbn, String title, String author, int year) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN invalide");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titre invalide");
        }
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    // Deux livres sont "le même livre" s'ils ont le même ISBN : c'est l'identifiant métier.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book other)) {
            return false;
        }
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return title + " - " + author + " (" + year + ") [ISBN " + isbn + "]";
    }
}
