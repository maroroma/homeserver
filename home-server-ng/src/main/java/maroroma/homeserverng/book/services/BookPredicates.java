package maroroma.homeserverng.book.services;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.Serie;

import java.util.*;
import java.util.function.*;

public class BookPredicates {

    public static Predicate<Book> belongToSerie(Serie serie) {
        return (oneBook) -> Optional.ofNullable(serie).map(Serie::getBookIds).map(list -> list.contains(oneBook.getId())).orElse(false);
    }
}
