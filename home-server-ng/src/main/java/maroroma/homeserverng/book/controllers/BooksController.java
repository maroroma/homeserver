package maroroma.homeserverng.book.controllers;

import maroroma.homeserverng.book.BookModuleDescriptor;
import maroroma.homeserverng.book.model.AddBookRequest;
import maroroma.homeserverng.book.model.BooksGroupedBySeries;
import maroroma.homeserverng.book.model.IsbnPhoto;
import maroroma.homeserverng.book.model.SearchResultsViaIsbnPhoto;
import maroroma.homeserverng.book.model.SendCollectionsStatusRequest;
import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.Serie;
import maroroma.homeserverng.book.model.importbatch.ImportBookProposal;
import maroroma.homeserverng.book.model.importbatch.ImportBookProposalsForSerieRequest;
import maroroma.homeserverng.book.model.importbatch.ImportFromPageRequest;
import maroroma.homeserverng.book.services.BookImporter;
import maroroma.homeserverng.book.services.BookService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller pour la gestion des books
 */
@HomeServerRestController(moduleDescriptor = BookModuleDescriptor.class)
public class BooksController {

    private final BookService bookService;

    private final BookImporter bookImporter;

    public BooksController(BookService bookService, BookImporter bookImporter) {
        this.bookService = bookService;
        this.bookImporter = bookImporter;
    }

    @GetMapping("${homeserver.api.path:}/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(this.bookService.getAllBooks());
    }

    @GetMapping("${homeserver.api.path:}/books/booksGroupedBySeries")
    public ResponseEntity<BooksGroupedBySeries> getBooksGroupedBySeries() {
        return ResponseEntity.ok(this.bookService.getBooksGroupedBySeries());
    }

    @GetMapping("${homeserver.api.path:}/books/{id}/picture")
    public void downloadBookCover(@PathVariable("id") final String bookId,
                                  final HttpServletResponse response) {
        this.bookService.getBookPicture(bookId, response);
    }

    @GetMapping("${homeserver.api.path:}/books/series/{id}/picture")
    public void downloadSerieCover(@PathVariable("id") final String serieId,
                                   final HttpServletResponse response) {
        this.bookService.getSeriePicture(serieId, response);
    }

    @PostMapping("${homeserver.api.path:}/books")
    public ResponseEntity<Boolean> addBooksToLibrary(@RequestBody AddBookRequest addBookRequest) {
        return ResponseEntity.ok(this.bookService.addBook(addBookRequest));
    }

    @DeleteMapping("${homeserver.api.path:}/books/{bookId}")
    public ResponseEntity<Boolean> addBooksToLibrary(@PathVariable("bookId") String bookIdToDelete) {
        return ResponseEntity.ok(this.bookService.deleteBook(bookIdToDelete));
    }

    @PutMapping("${homeserver.api.path:}/books")
    public ResponseEntity<Boolean> updateBook(@RequestBody Book updateBookRequest) {
        return ResponseEntity.ok(this.bookService.updateBook(updateBookRequest));
    }

    @PutMapping("${homeserver.api.path:}/books/series")
    public ResponseEntity<List<Serie>> updateSerie(@RequestBody Serie serieToUpdate) {
        return ResponseEntity.ok(this.bookService.updateSerie(serieToUpdate));
    }

    @PostMapping("${homeserver.api.path:}/books/series/book")
    public ResponseEntity<List<Serie>> searchSerieFromBook(@RequestBody Book bookCandidate) {
        return ResponseEntity.ok(this.bookService.searchExistingSeriesFromBook(bookCandidate));
    }

    @GetMapping("${homeserver.api.path:}/books/series")
    public ResponseEntity<List<Serie>> getAllSeries() {
        return ResponseEntity.ok(this.bookService.getAllSeries());
    }

    @GetMapping("${homeserver.api.path:}/books/series/{id}")
    public ResponseEntity<Serie> getOneSerie(@PathVariable("id") String serieId) {
        return ResponseEntity.ok(this.bookService.getSerie(serieId));
    }

    @DeleteMapping("${homeserver.api.path:}/books/series/{id}")
    public ResponseEntity<List<Serie>> deleteOneSerie(@PathVariable("id") String serieId) {
        return ResponseEntity.ok(this.bookService.deleteOneSerie(serieId));
    }


    @GetMapping("${homeserver.api.path:}/books/series/{id}/books")
    public ResponseEntity<List<Book>> getBooksFromeSerie(@PathVariable("id") String serieId) {
        return ResponseEntity.ok(this.bookService.getAllBooksForSerie(serieId));
    }


    @PostMapping("${homeserver.api.path:}/books/series")
    public ResponseEntity<List<Serie>> addNewSerie(@RequestBody Serie serieToAdd) throws HomeServerException {
        return ResponseEntity.ok(this.bookService.createSerie(serieToAdd));
    }

    @PostMapping("${homeserver.api.path:}/books/mail/missing")
    public ResponseEntity<Boolean> sendMailForUncompleteCollections(@RequestBody SendCollectionsStatusRequest sendCollectionsStatusRequest) {
        return ResponseEntity.ok(this.bookService.sendMailForUncompleteCollections(sendCollectionsStatusRequest));
    }


    @GetMapping("${homeserver.api.path:}/books/search/query/{query}")
    public ResponseEntity<List<Book>> searchBookFromGenericQuery(@PathVariable("query") String query) {
        return ResponseEntity.ok(this.bookService.getBookCandidateFromGenericSearch(query));
    }

    @PostMapping("${homeserver.api.path:}/books/search/isbn")
    public ResponseEntity<SearchResultsViaIsbnPhoto> searchBookFromIsbn(@RequestBody IsbnPhoto isbnPhoto) {
        return ResponseEntity.ok(this.bookService.getBookCandidatesFromIsbnPicture(isbnPhoto));
    }

    @PostMapping("${homeserver.api.path:}/books/import/serieUrl")
    public ResponseEntity<List<ImportBookProposal>> importBooksFromSerieResource(@RequestBody ImportFromPageRequest importFromPageRequest) {
        return ResponseEntity.ok(bookImporter.getBookProposalsFromSerieResource(importFromPageRequest));
    }

    @PostMapping("${homeserver.api.path:}/books/import/bookProposals")
    public ResponseEntity<Boolean> importBookProposalsIntoSerie(@RequestBody ImportBookProposalsForSerieRequest importBookProposalsForSerieRequest) {
        return ResponseEntity.ok(bookImporter.importBookProposalsIntoSerie(importBookProposalsForSerieRequest));
    }

    @GetMapping("${homeserver.api.path:}/books/search/isbn/{isbnToSearch}")
    public ResponseEntity<List<Book>> searchBookFromIsbn(@PathVariable("isbnToSearch") String isbn) {
//        return ResponseEntity.ok(List.of(Book.builder()
//                .id(UUID.randomUUID().toString())
//                        .author("author")
//                        .title("title")
//                        .subtitle("subtitle")
//                        .pageCount(32)
//                        .pictureFileId("pictureFileID")
//                        .isbns(List.of(isbn))
//                        .serieInfo(SerieInfo.builder()
//                                .serieId("seriedId")
//                                .serieName("serieName")
//                                .orderInSerie("3")
//                                .build())
//                .build(),
//                Book.builder()
//                        .id(UUID.randomUUID().toString())
//                        .author("author")
//                        .title("title2")
//                        .subtitle("subtitle")
//                        .pageCount(32)
//                        .pictureFileId("pictureFileID")
//                        .isbns(List.of(isbn))
//                        .initialImageLink("http://books.google.com/books/content?id=fpBtDgAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api")
//                        .serieInfo(SerieInfo.builder()
//                                .serieId("seriedId")
//                                .serieName("serieName")
//                                .orderInSerie("3")
//                                .build())
//                        .build()
//                ));


        return ResponseEntity.ok(this.bookService.getBookCandidatesFromIsbn(isbn));
    }

}
