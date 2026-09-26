package fr.library;

// Ce que le client envoie en JSON : un record se désérialise sans constructeur vide ni setters.
public record BookRequest(String isbn, String title, String author, int year) {

    public Book toBook() {
        return new Book(isbn, title, author, year);
    }
}
