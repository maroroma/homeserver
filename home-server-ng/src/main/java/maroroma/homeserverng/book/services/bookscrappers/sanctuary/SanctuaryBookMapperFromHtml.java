package maroroma.homeserverng.book.services.bookscrappers.sanctuary;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.SerieInfo;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapperSource;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SanctuaryBookMapperFromHtml {


    public Optional<Book> mapFromHtmlDocumentAndBasicBook(Document document) {

        return Optional.ofNullable(document.getElementsByClass("hero-title").first())
                .map(heroTitleElement -> {
                    String fullTitle = heroTitleElement.text();
                    String title = heroTitleElement.getElementsByTag("a").text();
                    String subtitle = heroTitleElement.getElementsByTag("span").text();
                    String number = fullTitle.replace(title, "").replace(subtitle, "").trim();
                    return Book.builder()
                            .title(title + " - " + number)
                            .subtitle(subtitle)
                            .scrapperSource(BookScrapperSource.SANCTUARY.name())
                            .serieInfo(SerieInfo.builder().orderInSerie(number).build());
                })
                .map(bookBuilder -> bookBuilder.id(UUID.randomUUID().toString()))
                .map(bookBuilder -> bookBuilder.isbns(extractIsbn(document)))
                .map(bookBuilder -> bookBuilder.author(extractAuthor(document)))
                .map(Book.BookBuilder::build);
    }

    private Optional<String> getTextFromElementId(Document document, String elementId) {
        return Optional.ofNullable(document.getElementById(elementId))
                .map(Element::text);
    }

    private String extractAuthor(Document document) {
        return getTextFromElementId(document, "dessinateur")
                .orElseGet(() -> getTextFromElementId(document, "scenariste")
                        .orElse(null));
    }

    private List<String> extractIsbn(Document document) {
        return document.getElementsByClass("affiliation")
                .stream()
                .filter(oneElement -> oneElement.hasAttr("ean"))
                .findFirst()
                .map(Element::attributes)
                .map(attributes -> attributes.get("ean"))
                .map(List::of)
                .orElse(Collections.emptyList());
    }

}
