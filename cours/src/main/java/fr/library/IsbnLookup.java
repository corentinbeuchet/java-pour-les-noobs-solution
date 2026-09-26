package fr.library;

import java.util.Optional;

// Appel à un service externe (API d'un éditeur…) : lent, payant, parfois indisponible.
public interface IsbnLookup {

    Optional<String> titleOf(String isbn);
}
