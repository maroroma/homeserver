package maroroma.homeserverng.book.services.bookscrappers;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.importbatch.ImportBookProposal;
import maroroma.homeserverng.book.model.importbatch.ImportFromPageRequest;

import java.util.*;

/**
 * Définition d'un service récupérant des informations sur des livres
 */
public interface BookScrapper {

    boolean isEnable();

    boolean isSource(BookScrapperSource bookScrapperSource);

    List<Book> findFromIsbn(String isbnCode);
    List<Book> findFromGenericSearch(String genericTermsForSearch);

    List<ImportBookProposal> listBooksFromSerieResource(ImportFromPageRequest importFromPageRequest);
}
