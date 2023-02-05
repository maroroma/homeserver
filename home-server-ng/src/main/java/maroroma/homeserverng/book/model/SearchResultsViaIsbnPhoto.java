package maroroma.homeserverng.book.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.book.model.custom.Book;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultsViaIsbnPhoto {
    private String scannedIsbn;
    private List<Book> books;

    public static SearchResultsViaIsbnPhoto notFound() {
        return SearchResultsViaIsbnPhoto.builder().scannedIsbn("NO ISBN SCANNED")
                .books(Collections.emptyList())
                .build();
    }
}
