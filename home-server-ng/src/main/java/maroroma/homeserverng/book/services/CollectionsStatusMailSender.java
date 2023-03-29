package maroroma.homeserverng.book.services;

import maroroma.homeserverng.book.model.BooksGroupedBySeries;
import maroroma.homeserverng.book.model.CollectionStatusForSending;
import maroroma.homeserverng.book.model.SendCollectionsStatusRequest;
import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.Serie;
import maroroma.homeserverng.book.model.custom.SerieInfo;
import maroroma.homeserverng.book.model.custom.SerieWithFullBooks;
import maroroma.homeserverng.config.MailConfigHolder;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.helpers.StringUtils;
import maroroma.homeserverng.tools.template.TemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Component
public class CollectionsStatusMailSender {

    public static final String SENDING_TITLE = "Les bouquins manquants";
    /**
     * Effort de centralisation de la configuration des mails.
     */
    private final MailConfigHolder mailConfigHolder;

    @Property("homeserver.notifyer.templates.path")
    private HomeServerPropertyHolder templatesDirectory;

    public CollectionsStatusMailSender(MailConfigHolder mailConfigHolder) {
        this.mailConfigHolder = mailConfigHolder;
    }


    public void sendMailWithSeriesStatus(SendCollectionsStatusRequest sendCollectionsStatusRequest,
                                         BooksGroupedBySeries booksGroupedBySeries,
                                         List<Serie> allSeries
    ) {

        // les ids des series où des tomes sont manquants
        List<String> notCompletedSerieIds = allSeries.stream()
                .filter(Predicate.not(Serie::isCompleted))
                .map(Serie::getId)
                .toList();

        List<CollectionStatusForSending> collectionStatusForSendings = booksGroupedBySeries.getBooksWithSerie()
                .stream()
                // on ne garde que celles qui ne sont pas complétées
                .filter(oneSerieWithBooks -> notCompletedSerieIds.contains(oneSerieWithBooks.getId()))
                .sorted((serie1, serie2) -> serie1.getTitle().compareToIgnoreCase(serie2.getTitle()))
                .map(oneSerieWithBooks -> CollectionStatusForSending.builder()
                        .serieTitle(oneSerieWithBooks.getTitle())
                        .maxBookInSerie(calculateMaxNbBooks(oneSerieWithBooks))
                        .currentBooksInSerie(oneSerieWithBooks.getBooks().size())
                        .estimatedMissingBook(calculateMissingBookInSerie(oneSerieWithBooks))
                        .build())
                .toList();

        String fullMailBody = TemplateBuilder.create()
                .withTemplate(this.templatesDirectory.getResolvedValue() + "/missing-books.html")
                .addParameter("title", SENDING_TITLE)
                .addParameter("message", SENDING_TITLE)
                .addArrayParameter("seriesBlock",
                        this.templatesDirectory.getResolvedValue() + "/missing-books-serie-subtemplate.html",
                        collectionStatusForSendings,
                        (subTemplateBuilder, oneItem) -> subTemplateBuilder
                                .addParameter("serieTitle", oneItem.getSerieTitle())
                                .addParameter("currentBooksInSerie", oneItem.getCurrentBooksInSerie())
                                .addParameter("maxBookInSerie", oneItem.getMaxBookInSerie())
                                .addArrayParameter("estimatedMissingBooks",
                                        this.templatesDirectory.getResolvedValue() + "/estimated-missing-books-subtemplate.html",
                                        oneItem.getEstimatedMissingBook(),
                                        (subTemplateBuilderForMissingBooks, oneMissingBookNumber) -> subTemplateBuilderForMissingBooks.addParameter("oneMissingBook", oneMissingBookNumber)
                                )
                )
                .resolve();


        JavaMailSender sender = this.mailConfigHolder.createSender();

        sender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(SENDING_TITLE);
            mimeMessageHelper.setTo(sendCollectionsStatusRequest.getEmails().toArray(String[]::new));
            mimeMessageHelper.setFrom("homeserverrkt");
            mimeMessageHelper.setText(fullMailBody, true);
            mimeMessageHelper.addInline("imageResourceName",
                    new FileSystemResource(this.templatesDirectory.getResolvedValue() +"/assets/home.png"));
        });
    }

    private int calculateMaxNbBooks(SerieWithFullBooks oneSerieWithBooks) {
        return oneSerieWithBooks.getBooks()
                .stream()
                .map(Book::getSerieInfo)
                .map(SerieInfo::getOrderInSerie)
                .filter(Objects::nonNull)
                .filter(StringUtils::isInteger)
                .map(Integer::parseInt)
                .max(Integer::compareTo)
                .orElse(0);

    }

    private List<Integer> calculateMissingBookInSerie(SerieWithFullBooks serieWithFullBooks) {
        List<Integer> actualVolumes = serieWithFullBooks.getBooks()
                .stream()
                .map(Book::getSerieInfo)
                .map(SerieInfo::getOrderInSerie)
                .filter(Objects::nonNull)
                .filter(StringUtils::isInteger)
                .map(Integer::parseInt)
                .sorted()
                .toList();

        return IntStream.range(1, CollectionUtils.lastElement(actualVolumes))
                .filter(oneVolume -> !actualVolumes.contains(oneVolume))
                .boxed()
                .toList();
    }
}
