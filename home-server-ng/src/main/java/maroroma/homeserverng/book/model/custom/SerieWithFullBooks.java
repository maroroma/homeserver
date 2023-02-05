package maroroma.homeserverng.book.model.custom;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SerieWithFullBooks {
    private String id;
    private String title;
    private List<Book> books;

    public static SerieWithFullBooks booksWithoutSerie(List<Book> booksWithoutSerie) {
        return SerieWithFullBooks.builder()
                .id("BOOKS_WITHOUT_SERIES")
                .title("BOOKS_WITHOUT_SERIES")
                .books(booksWithoutSerie).build();
    }
}
