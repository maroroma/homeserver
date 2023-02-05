package maroroma.homeserverng.book.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionStatusForSending {
    private String serieTitle;
    private int currentBooksInSerie;
    private List<Integer> estimatedMissingBook;
    private int maxBookInSerie;
}
