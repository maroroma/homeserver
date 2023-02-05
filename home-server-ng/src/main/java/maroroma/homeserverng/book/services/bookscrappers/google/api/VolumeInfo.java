package maroroma.homeserverng.book.services.bookscrappers.google.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VolumeInfo {
    private String title;
    private String subtitle;
    private List<String> authors;
    private List<IsbnIdentifier> industryIdentifiers;
    private int pageCount;
    private ImageLinks imageLinks;
    private SeriesInfo seriesInfo;
}
