package maroroma.homeserverng.book.services;

import maroroma.homeserverng.book.model.AddBookRequest;
import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.SerieInfo;
import maroroma.homeserverng.book.model.importbatch.ImportBookProposal;
import maroroma.homeserverng.book.model.importbatch.ImportBookProposalsForSerieRequest;
import maroroma.homeserverng.book.model.importbatch.ImportFromPageRequest;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapper;
import maroroma.homeserverng.tools.exceptions.Traper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookImporter {

    private final List<BookScrapper> bookScrappers;
    private final BookService bookService;

    public BookImporter(List<BookScrapper> bookScrappers, BookService bookService) {
        this.bookScrappers = bookScrappers;
        this.bookService = bookService;
    }

    public List<ImportBookProposal> getBookProposalsFromSerieResource(ImportFromPageRequest importFromPageRequest) {
        // mise à jour de l'url de la série
        var serieToUpdate = importFromPageRequest.getSerie();
        serieToUpdate.setSerieUrlForImport(importFromPageRequest.getSeriePageUrl());

        this.bookService.updateSerie(serieToUpdate);

        // import depuis l'url fournie
        List<ImportBookProposal> allResults = bookScrappers.stream()
                .filter(bookScrapper -> bookScrapper.isSource(importFromPageRequest.getBookScrapperSource()))
                .flatMap(bookScrapper -> bookScrapper.listBooksFromSerieResource(importFromPageRequest).stream())
                .toList();

        // on flague les book déjà dans la collection
        List<Integer> bookOrderInSerie = this.bookService.getAllBooksForSerie(importFromPageRequest.getSerie())
                .stream()
                .map(oneBook -> oneBook.getSerieInfo().getOrderInSerie())
                .map(oneRawOrder -> Traper.trapOr(() -> Integer.parseInt(oneRawOrder), () -> 0))
                .toList();

        return allResults.stream()
                .map(oneBookProposal -> {
                    oneBookProposal.setAlreadyInCollection(bookOrderInSerie.contains(oneBookProposal.getNumber()));
                    return oneBookProposal;
                })
                .toList();


    }

    public boolean importBookProposalsIntoSerie(ImportBookProposalsForSerieRequest importBookProposalsForSerieRequest) {
        return bookService.addBook(AddBookRequest.builder()
                .serieIdToAssociateTo(importBookProposalsForSerieRequest.getTargetedSerie().getId())
                .booksToAdd(this.mapBookProposalsToBook(importBookProposalsForSerieRequest))
                .build());
    }

    private List<Book> mapBookProposalsToBook(ImportBookProposalsForSerieRequest importBookProposalsForSerieRequest) {
        return importBookProposalsForSerieRequest.getBooksToImport()
                .stream()
                .map(oneProposal -> Book.builder()
                        .id(UUID.randomUUID().toString())
                        .initialImageLink(oneProposal.getInitialImageLink())
                        .title(oneProposal.getTitle())
                        .owner(importBookProposalsForSerieRequest.getOwner())
                        .subtitle(importBookProposalsForSerieRequest.getImportPrefix() + " " + oneProposal.getNumber())
                        .serieInfo(SerieInfo.builder().orderInSerie(Integer.toString(oneProposal.getNumber())).build()))
                .map(Book.BookBuilder::build)
                .toList();
    }




}
