package maroroma.homeserverng.book.services;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.Serie;

import java.util.function.*;

public class BookPredicates {

    public static Predicate<Book> belongToSerie(Serie serie) {
        return (oneBook) -> serie.getBookIds().contains(oneBook.getId());
    }
}
