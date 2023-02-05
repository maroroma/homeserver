package maroroma.homeserverng.book.services.bookscrappers.sanctuary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import maroroma.homeserverng.book.services.bookscrappers.sanctuary.api.OneBook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BDSanctuaryScrapperTest {

    @Test
    public void testFindFromIsbn() throws IOException {

        File htmlFile = new File("C:\\tmp\\test_scrapper.htm");
        Document document = Jsoup.parse(htmlFile);
        Element herotitle = document.getElementsByClass("hero-title").first();
        String texteComplet = herotitle.text();
        String titre = herotitle.getElementsByTag("a").text();
        String subtitle = herotitle.getElementsByTag("span").text();
        String number = texteComplet.replace(titre, "").replace(subtitle, "").trim();

        String artist = document.getElementById("dessinateur").text();
        String scenarist = document.getElementById("scenariste").text();

        String isbn = document.getElementsByClass("affiliation")
                .stream()
                .filter(oneElement -> oneElement.hasAttr("ean"))
                .findFirst()
                .map(Element::attributes)
                .map(attributes -> attributes.get("ean"))
                .orElse("");


        System.out.println(texteComplet);

    }
}