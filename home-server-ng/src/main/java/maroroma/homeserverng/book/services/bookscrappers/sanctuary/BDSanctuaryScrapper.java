package maroroma.homeserverng.book.services.bookscrappers.sanctuary;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapper;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;
import maroroma.homeserverng.book.services.bookscrappers.sanctuary.api.OneBook;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class BDSanctuaryScrapper implements BookScrapper {

    private static final String SEARCH_URI_FOR_ISBN = "core/inc/recherche.php?term=";


    @Property("homeserver.books.sanctuary.api.url")
    private HomeServerPropertyHolder sanctuaryRootUrl;

    @Property("homeserver.books.sanctuary.enable")
    private HomeServerPropertyHolder isScrapperEnabled;

    private final SanctuaryBookMapperFromApi sanctuaryBookMapperFromApi;
    private final SanctuaryBookMapperFromHtml sanctuaryBookMapperFromHtml;

    private final ObjectMapper objectMapper;

    public BDSanctuaryScrapper(SanctuaryBookMapperFromApi sanctuaryBookMapperFromApi,
                               SanctuaryBookMapperFromHtml sanctuaryBookMapperFromHtml) {
        this.sanctuaryBookMapperFromApi = sanctuaryBookMapperFromApi;
        this.sanctuaryBookMapperFromHtml = sanctuaryBookMapperFromHtml;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Override
    public boolean isEnable() {
        return isScrapperEnabled.asBoolean();
    }

    @Override
    public List<Book> findFromIsbn(String isbnCode) {
        return this.getSimpleBooksFromSanctuary(isbnCode)
                .stream()
                .map(this::generateBookFromHtmlPage)
                .toList();
    }

    @Override
    public List<Book> findFromGenericSearch(String genericTermsForSearch) {
        return this.findFromIsbn(genericTermsForSearch);
    }

    /**
     * Récupération des livres depuis la recherche par isbn sanctuary (réponse cheap)
     *
     * @param searchTerm critère de recherche
     * @return
     */
    private List<OneBook> getSimpleBooksFromSanctuary(String searchTerm) {
        RestTemplate restTemplate = new RestTemplate();
        return Optional.of(restTemplate
                        .getForEntity(sanctuaryRootUrl.getResolvedValue()
                                        + SEARCH_URI_FOR_ISBN
                                        + searchTerm,
                                String.class))
                .map(ResponseEntity::getBody)
                .map(Traper.trapAndMap(rawBody -> objectMapper.readValue(rawBody, OneBook[].class)))
                .map(List::of)
                .orElseGet(Collections::emptyList);
    }

    /**
     * Mapping d'un {@link Book} à partir de la page web de sanctuary pointée par le {@link OneBook} en entrée
     *
     * @param oneBasicBook
     * @return
     */
    private Book generateBookFromHtmlPage(OneBook oneBasicBook) {
        return Optional.ofNullable(oneBasicBook)
                .map(OneBook::getUrl)
                .flatMap(urlToParse -> Traper.trapWithOptional(() -> Jsoup
                        .connect(this.sanctuaryRootUrl.getResolvedValue() + urlToParse).get()))
                .flatMap(sanctuaryBookMapperFromHtml::mapFromHtmlDocumentAndBasicBook)
                .orElseGet(() -> this.sanctuaryBookMapperFromApi.mapBooksFromSanctuaryResponse(oneBasicBook));

    }


}
