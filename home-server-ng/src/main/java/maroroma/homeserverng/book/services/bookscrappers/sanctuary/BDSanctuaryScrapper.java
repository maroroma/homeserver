package maroroma.homeserverng.book.services.bookscrappers.sanctuary;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.importbatch.ImportBookProposal;
import maroroma.homeserverng.book.model.importbatch.ImportFromPageRequest;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapper;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;
import maroroma.homeserverng.book.services.bookscrappers.sanctuary.api.OneBook;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;


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
    public boolean isSource(BookScrapperSource bookScrapperSource) {
        return bookScrapperSource == BookScrapperSource.SANCTUARY;
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

    @Override
    public List<ImportBookProposal> listBooksFromSerieResource(ImportFromPageRequest importFromPageRequest) {

        return Optional.ofNullable(importFromPageRequest.getSeriePageUrl())
                .flatMap(urlToParse -> Traper.trapWithOptional(() -> Jsoup
//                        .parse(new File("C:\\Workspaces\\PERSO\\HOMESERVER\\homeserver\\home-server-ng\\src\\main\\java\\maroroma\\homeserverng\\book\\services\\bookscrappers\\sanctuary\\serie_detail_sanctuary.html")))
                        .connect(urlToParse).get())
                )
                .map(document -> document.getElementsByClass("edition-objet"))
                .map(allObjectsForSerie -> mapObjectsToProposals(allObjectsForSerie, importFromPageRequest))
                .orElse(List.of());
    }

    private List<ImportBookProposal> mapObjectsToProposals(Elements elements, ImportFromPageRequest importFromPageRequest) {
        return elements.stream().map(oneElement -> {
            Element image = oneElement.getElementsByClass("card-img-top").get(0);
            Element number = oneElement.getElementsByClass("badge btn-blue-gray").get(0);

            return ImportBookProposal.builder()
                    .title(importFromPageRequest.getSerie().getTitle())
                    .initialImageLink(image.attr("src"))
                    .number(Integer.parseInt(number.html().replace("#", "")))
                    .build();
        }).toList();
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
