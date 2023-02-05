package maroroma.homeserverng.book.services.bookscrappers.google;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.SerieInfo;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;
import maroroma.homeserverng.book.services.bookscrappers.google.api.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GoogleBookMapper {

    public List<Book> mapFromGoogleResponse(SearchBookResponse searchBookResponse) {
        return Optional.ofNullable(searchBookResponse)
                .map(SearchBookResponse::getItems)
                .stream()
                .flatMap(Collection::stream)
                .map(this::mapOneBookFromGoogle)
                .toList();
    }

    private Book mapOneBookFromGoogle(OneBook bookFromGoogle) {
        VolumeInfo volumeInfo = bookFromGoogle.getVolumeInfo();
        return Book.builder()
                .id(UUID.randomUUID().toString())
                .author(CollectionUtils.firstElement(volumeInfo.getAuthors()))
                .initialImageLink(mapToSimpleThumbnailUrl(volumeInfo))
                .isbns(mapToSimpleIsbn(volumeInfo))
                .pageCount(volumeInfo.getPageCount())
                .title(volumeInfo.getTitle())
                .subtitle(volumeInfo.getSubtitle())
                .serieInfo(mapToSimpleSerieInfo(volumeInfo))
                .scrapperSource(BookScrapperSource.GOOGLE.name())
                .build();
    }
    private String mapToSimpleThumbnailUrl(VolumeInfo volumeInfo) {
        return Optional.of(volumeInfo)
                .map(VolumeInfo::getImageLinks)
                .map(ImageLinks::getSmallThumbnail)
                .orElse(null);
    }

    private List<String> mapToSimpleIsbn(VolumeInfo volumeInfo) {
        return Optional.of(volumeInfo.getIndustryIdentifiers())
                .stream()
                .flatMap(Collection::stream)
                .map(IsbnIdentifier::getIdentifier)
                .toList();
    }

    private SerieInfo mapToSimpleSerieInfo(VolumeInfo volumeInfo) {
        return Optional.ofNullable(volumeInfo)
                .map(VolumeInfo::getSeriesInfo)
                .map(seriesInfo -> SerieInfo.builder()
                        .serieId(CollectionUtils.firstElement(seriesInfo.getVolumeSeries()).getSeriesId())
                        .orderInSerie(seriesInfo.getBookDisplayNumber())
                        .build())
                .orElse(null);
    }


}
