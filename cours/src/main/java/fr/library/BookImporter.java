package fr.library;

public class BookImporter {

    private final IsbnLookup lookup;
    private final Library library;

    public BookImporter(IsbnLookup lookup, Library library) {
        this.lookup = lookup;
        this.library = library;
    }

    public Book importBook(String isbn, String author, int year) {
        String title = lookup.titleOf(isbn)
                .orElseThrow(() -> new BookNotFoundException("ISBN inconnu : " + isbn));
        Book book = new Book(isbn, title, author, year);
        library.addBook(book);
        return book;
    }
}
