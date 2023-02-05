package maroroma.homeserverng.book.services;


import maroroma.homeserverng.book.model.custom.Book;
import org.junit.Test;

import java.util.List;

/**
 * Classe de test pour {@link BookSearchResultSorter}.
 */
public class BookSearchResultSorterTest {

    @Test
    public void test() {
        Book book1 = Book.builder()
                .initialImageLink("test")
                .title("title")
                .subtitle("subtitle")
                .author("author")
                .build();

        Book book2 = Book.builder()
                .title("title")
                .subtitle("subtitle")
                .author("author")
                .build();

        Book book3 = Book.builder()
                .title("title")
                .subtitle("subtitle")
                .build();

        Book book4 = Book.builder()
                .title("title")
                .author("author")
                .build();

        Book book5 = Book.builder()
                .title("title")
                .author("author")
                .build();

        Book book6 = Book.builder()
                .author("author")
                .build();
        Book book7 = Book.builder()
                .subtitle("subtitle")
                .author("author")
                .build();

        List<Book> books = List.of(book5, book4, book2, book1, book3, book7, book5);

        List<Book> sorted = books.stream().sorted(new BookSearchResultSorter()).toList();

        System.out.print(sorted);

    }

}