package maroroma.homeserverng.book.services.bookscrappers.google;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapper;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;
import maroroma.homeserverng.book.services.bookscrappers.google.api.SearchBookResponse;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class GoogleBookScrapper implements BookScrapper {

    private static final String SEARCH_URI_FOR_ISBN = "volumes?q=isbn:";
    private static final String SEARCH_URI_FOR_GENERIC_SEARCH = "volumes?q=";


    private final GoogleBookMapper googleBookMapper;


    @Property("homeserver.books.googleapi.url")
    private HomeServerPropertyHolder googleApiRootUrl;

    @Property("homeserver.books.googleapi.enable")
    private HomeServerPropertyHolder isGoogleApiEnabled;


    public GoogleBookScrapper(GoogleBookMapper googleBookMapper) {
        this.googleBookMapper = googleBookMapper;
    }


    @Override
    public boolean isEnable() {
        return isGoogleApiEnabled.asBoolean();
    }

    @Override
    public List<Book> findFromIsbn(String isbnCode) {
        RestTemplate restTemplate = new RestTemplate();
        SearchBookResponse searchBookResponse = restTemplate
                .getForEntity(googleApiRootUrl.getResolvedValue()
                                + SEARCH_URI_FOR_ISBN
                                + isbnCode,
                        SearchBookResponse.class)
                .getBody();

        List<Book> resultsFromIsbnSearch = googleBookMapper.mapFromGoogleResponse(searchBookResponse);

        List<Book> moreResultFromFirstIsbnResult = Optional.ofNullable(CollectionUtils.firstElement(resultsFromIsbnSearch))
                .map(firstResult -> {
                    String author = firstResult.getAuthor();
                    String title = firstResult.getTitle();

                    return restTemplate
                            .getForEntity(googleApiRootUrl.getResolvedValue()
                                            + SEARCH_URI_FOR_GENERIC_SEARCH
                                            + String.format("%s %s", author, title),
                                    SearchBookResponse.class)
                            .getBody();
                })
                .map(googleBookMapper::mapFromGoogleResponse)
                .orElse(Collections.emptyList());


        return Stream.concat(resultsFromIsbnSearch.stream(), moreResultFromFirstIsbnResult.stream()).toList();
    }

    @Override
    public List<Book> findFromGenericSearch(String genericTermsForSearch) {
        RestTemplate restTemplate = new RestTemplate();

        SearchBookResponse searchBookResponse = restTemplate
                .getForEntity(googleApiRootUrl.getResolvedValue()
                                + SEARCH_URI_FOR_GENERIC_SEARCH
                                + genericTermsForSearch,
                        SearchBookResponse.class)
                .getBody();

        return this.googleBookMapper.mapFromGoogleResponse(searchBookResponse);
    }

}
