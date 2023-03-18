package maroroma.homeserverng.book.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.book.model.custom.SerieWithFullBooks;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BooksGroupedBySeries {
    private List<SerieWithFullBooks> booksWithSerie;
    private SerieWithFullBooks booksWithoutSerie;
}
