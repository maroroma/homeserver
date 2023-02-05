package maroroma.homeserverng.book.services;

import maroroma.homeserverng.book.model.custom.Book;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Comparator;

/**
 * Comparateur de résultat de recherche de livres
 * Va privilégier les résultats pour lesquels on a une couverture
 */
@Component
public class BookSearchResultSorter implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        boolean book1hasInitialImage = StringUtils.hasLength(o1.getInitialImageLink());
        boolean book2hasInitialImage = StringUtils.hasLength(o2.getInitialImageLink());

        return Boolean.compare(book1hasInitialImage, book2hasInitialImage);

    }
}
