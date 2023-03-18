package maroroma.homeserverng.book.model.importbatch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.book.model.custom.Serie;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportFromPageRequest {
    private BookScrapperSource bookScrapperSource;
    private String seriePageUrl;
    private Serie serie;


}
