package maroroma.homeserverng.book.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.book.model.custom.Book;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBookRequest {
    private List<Book> booksToAdd;
    private String serieIdToAssociateTo;
}
