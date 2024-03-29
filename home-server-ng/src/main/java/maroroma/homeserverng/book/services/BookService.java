package maroroma.homeserverng.book.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.book.model.AddBookRequest;
import maroroma.homeserverng.book.model.BooksGroupedBySeries;
import maroroma.homeserverng.book.model.IsbnPhoto;
import maroroma.homeserverng.book.model.SearchResultsViaIsbnPhoto;
import maroroma.homeserverng.book.model.SendCollectionsStatusRequest;
import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.Serie;
import maroroma.homeserverng.book.model.custom.SerieInfo;
import maroroma.homeserverng.book.model.custom.SerieWithFullBooks;
import maroroma.homeserverng.book.services.bookscrappers.BookScrapper;
import maroroma.homeserverng.filemanager.services.FileManagerServiceImpl;
import maroroma.homeserverng.filemanager.services.FilesFactory;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.barcode.BarCodeReader;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.files.FileDescriptor;
import maroroma.homeserverng.tools.files.FileDirectoryDescriptor;
import maroroma.homeserverng.tools.files.FileOperationResult;
import maroroma.homeserverng.tools.helpers.Predicates;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javax.servlet.http.HttpServletResponse;

/**
 * Service de gestion des livres et bd et mangas
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookUpdaterService bookUpdaterService;
    private final FileManagerServiceImpl fileManagerService;
    private final FilesFactory filesFactory;
    private final BookSearchResultSorter bookSearchResultSorter;
    private final List<BookScrapper> bookScrappers;
    private final CollectionsStatusMailSender collectionsStatusMailSender;


    @Property("homeserver.books.image.library")
    private HomeServerPropertyHolder bookPicturesDirectoryPropertyHolder;

    @InjectNanoRepository(
            file = @Property("homeserver.books.series.store"),
            persistedType = Serie.class)
    private NanoRepository seriesRepo;

    @InjectNanoRepository(
            file = @Property("homeserver.books.store"),
            persistedType = Book.class)
    private NanoRepository booksRepo;

    public SearchResultsViaIsbnPhoto getBookCandidatesFromIsbnPicture(IsbnPhoto isbnPhoto) {

        return Optional.ofNullable(isbnPhoto)
                .map(IsbnPhoto::getPhotoAsBase64)
                .map(rawData -> rawData.split(","))
                .map(rawData -> rawData[1])
                .map(Base64Utils::decodeFromString)
                .map(ByteArrayInputStream::new)
                .flatMap(BarCodeReader::readBarCode)
                .map(isbnResult -> SearchResultsViaIsbnPhoto.builder()
                        .scannedIsbn(isbnResult)
                        .books(this.getBookCandidatesFromIsbn(isbnResult))
                        .build())
                .orElseGet(SearchResultsViaIsbnPhoto::notFound);
    }

    public List<Book> getBookCandidatesFromIsbn(String isbnToFind) {
        return this.bookScrappers.stream()
                .filter(BookScrapper::isEnable)
                .map(oneBookScrapper -> oneBookScrapper.findFromIsbn(isbnToFind))
                .flatMap(List::stream)
                .sorted(this.bookSearchResultSorter)
                .collect(Collectors.toList());
    }

    public List<Book> getBookCandidateFromGenericSearch(String genericSearch) {
        return this.bookScrappers.stream()
                .filter(BookScrapper::isEnable)
                .map(oneBookScrapper -> oneBookScrapper.findFromGenericSearch(genericSearch))
                .flatMap(List::stream)
                .sorted(this.bookSearchResultSorter)
                .collect(Collectors.toList());
    }

    public List<Serie> searchExistingSeriesFromBook(Book bookCandidate) {
        return this.seriesRepo
                .<Serie>findAll(oneSerie ->
                        oneSerie.getTitle().toLowerCase()
                                .contains(bookCandidate.getTitle().toLowerCase())
                );
    }

    public List<Serie> getAllSeries() {
        return this.seriesRepo.getAll();
    }

    public List<Book> getAllBooks() {
        return this.booksRepo.getAll();
    }


    /**
     * Retourne l'ensemble des livres en les regroupant par série
     *
     * @return
     */
    public BooksGroupedBySeries getBooksGroupedBySeries() {
        List<Book> books = this.getAllBooks();

        List<SerieWithFullBooks> booksWithSerie = books.stream()
                .filter(Book::hasUsableSerieInfo)
                // on ne garde que les series avec des infos exploitables pour le regroupement
                .collect(Collectors.groupingBy(oneBook -> oneBook.getSerieInfo().getSerieId()))
                .entrySet()
                .stream()
                .map(oneEntrySet -> SerieWithFullBooks.builder()
                        .id(oneEntrySet.getKey())
                        .title(CollectionUtils.firstElement(oneEntrySet.getValue()).getSerieInfo().getSerieName())
                        .books(oneEntrySet.getValue())
                        .build())
                .toList();

        return BooksGroupedBySeries.builder()
                .booksWithSerie(booksWithSerie)
                .booksWithoutSerie(SerieWithFullBooks.booksWithoutSerie(
                        books.stream()
                                .filter(Predicate.not(Book::hasUsableSerieInfo))
                                .toList()))
                .build();

    }


    List<Book> getAllBooksForSerie(Serie serie) {
        return this.booksRepo.findAll(BookPredicates.belongToSerie(this.seriesRepo.findByIdMandatory(serie.getId())));
    }

    public List<Book> getAllBooksForSerie(String serieId) {
        return this.getAllBooksForSerie(this.seriesRepo.<Serie>findByIdMandatory(serieId));
    }


    public List<Serie> createSerie(Serie serieToAdd) {

        // on tente d'éviter les doublons ici
        if (this.seriesRepo.<Serie>find(oneSerie -> oneSerie.getTitle().trim().equalsIgnoreCase(serieToAdd.getTitle().trim())).isPresent()) {
            throw new RuntimeHomeServerException("Serie déjà existante : impossible d'ajouter");
        }


        serieToAdd.setId(UUID.randomUUID().toString());
        this.seriesRepo.save(serieToAdd);
        return this.seriesRepo.getAll();
    }

    public boolean addBook(AddBookRequest addBookRequest) {

        // le serie infoApplyer permet de remonter les informations de la série au niveau
        // du livre si elle a été associée
        // si pas de série, l'opérator ne fera rien
        UnaryOperator<Book> serieInfoApplyer = Optional.ofNullable(addBookRequest)
                .map(AddBookRequest::getSerieIdToAssociateTo)
                .map(this.seriesRepo::<Serie>findByIdMandatory)
                .map(SerieInfo::fromSerie)
                .map(oneSerieInfo -> {
                    return (UnaryOperator<Book>) book -> book.associateToSerie(oneSerieInfo);
                })
                .orElse(UnaryOperator.identity());

        // création des livres dans la base
        List<Book> bookIdsToAppendToSerie = Optional.ofNullable(addBookRequest)
                .map(AddBookRequest::getBooksToAdd)
                .stream()
                .flatMap(Collection::stream)
                // application des informations relatives à la série
                .map(serieInfoApplyer)
                .map(this::dowloadBookPictureAndUpdateBook)
                .map(this.booksRepo::saveAndReturn)
                .toList();

        Optional.ofNullable(addBookRequest)
                .map(AddBookRequest::getSerieIdToAssociateTo)
                .map(this.seriesRepo::<Serie>findByIdMandatory)
                .map(serieFromDatabase -> serieFromDatabase.appendBooksToSerie(bookIdsToAppendToSerie))
                .ifPresent(this.seriesRepo::update);

        return true;

    }


    public boolean updateBook(Book bookWithUpdatedInformations) {
        return this.bookUpdaterService.updateBook(bookWithUpdatedInformations);
    }

    public List<Serie> updateSerie(Serie toUpdate) {
        Serie fromRepo = this.seriesRepo.findByIdMandatory(toUpdate.getId());
        fromRepo.setCompleted(toUpdate.isCompleted());
        fromRepo.setSerieUrlForImport(toUpdate.getSerieUrlForImport());
        return this.seriesRepo.update(fromRepo);
    }

    public boolean deleteBook(String bookId) {
        Book bookFromRepoToDelete = this.booksRepo.findByIdMandatory(bookId);

        // nettoyage de la série si trouvée
        Optional.ofNullable(bookFromRepoToDelete)
                .map(Book::getSerieInfo)
                .map(SerieInfo::getSerieId)
                // on ne casse pas si la série n'est pas trouvée (c'est sans doute pour ça qu'on fait du ménage
                .flatMap(this.seriesRepo::<Serie>findById)
                .map(serieToUpdate -> serieToUpdate.removeBookFromSerie(bookFromRepoToDelete))
                .ifPresent(this.seriesRepo::update);

        this.booksRepo.delete(bookId);

        return true;
    }

    public void getBookPicture(String bookId, HttpServletResponse response) {

        this.fileManagerService.getFile(() -> this.booksRepo
                .<Book>findById(bookId)
                .map(Book::getPictureFileId), response);

    }

    public void getSeriePicture(String serieId, HttpServletResponse response) {

        this.fileManagerService.getFile(() -> this.seriesRepo
                .<Serie>findById(serieId)
                .map(Serie::getPictureFileId), response);
    }

    public boolean sendMailForUncompleteCollections(SendCollectionsStatusRequest sendCollectionsStatusRequest) {

        this.collectionsStatusMailSender.sendMailWithSeriesStatus(sendCollectionsStatusRequest,
                this.getBooksGroupedBySeries(),
                this.getAllSeries()
                );

        return true;
    }

    /**
     * Télécharge une image pour une brique si une url est renseignée
     */
    private Book dowloadBookPictureAndUpdateBook(Book bookToUpdate) {
        // dans le doute on créé l'arbo
        FileDirectoryDescriptor bookPicturesDirectory = this.filesFactory
                .directoryFromProperty(this.bookPicturesDirectoryPropertyHolder);
        bookPicturesDirectory.mkdirs();

        FileDescriptor bookPictureFile = bookPicturesDirectory
                .combinePath(bookToUpdate.getId() + ".bookPicture")
                .asFile();

        // si url image renseignée, download
        Optional.ofNullable(bookToUpdate.getInitialImageLink())
                .filter(Predicates.not(String::isEmpty))
                .map(Traper.trapAndMap(URL::new))
                .map(Traper.trapAndMap(URL::openStream))
                .map(bookPictureFile::copyFrom)
                .filter(FileOperationResult::isCompleted)
                .map(FileOperationResult::getInitialFile)
                .map(FileDescriptor::getId)
                // si download ok
                .ifPresent(bookToUpdate::setPictureFileId);

        return bookToUpdate;

    }

    public Serie getSerie(String serieId) {
        return this.seriesRepo.findByIdMandatory(serieId);
    }

    public List<Serie> deleteOneSerie(String serieId) {
        Serie serieToDelete = this.seriesRepo.findByIdMandatory(serieId);

        this.booksRepo.delete(BookPredicates.belongToSerie(serieToDelete));

        return this.seriesRepo.delete(serieId);
    }


}
