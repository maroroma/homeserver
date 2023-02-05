package maroroma.homeserverng.book.services.bookscrappers;

import maroroma.homeserverng.book.model.custom.Book;

import java.util.List;

/**
 * Définition d'un service récupérant des informations sur des livres
 */
public interface BookScrapper {

    boolean isEnable();
    List<Book> findFromIsbn(String isbnCode);
    List<Book> findFromGenericSearch(String genericTermsForSearch);
}
