package maroroma.homeserverng.book.services;

import maroroma.homeserverng.book.model.custom.Book;
import maroroma.homeserverng.book.model.custom.Serie;
import maroroma.homeserverng.book.model.custom.SerieInfo;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.helpers.Assert;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookUpdaterService {
    @InjectNanoRepository(
            file = @Property("homeserver.books.series.store"),
            persistedType = Serie.class)
    private NanoRepository seriesRepo;

    @InjectNanoRepository(
            file = @Property("homeserver.books.store"),
            persistedType = Book.class)
    private NanoRepository booksRepo;


    public boolean updateBook(Book bookWithUpdatedInformations) {

        Assert.notNull(bookWithUpdatedInformations, "book to update can't be null");

        // récupération du book initial
        // c'est lui qui sera modifié puis réenregistré
        Book bookFromRepoThatIsGoingToBeUpdated = this.booksRepo.findByIdMandatory(bookWithUpdatedInformations.getId());

        // série initialement affectée issue du repo (optionelle)
        Optional<Serie> serieFromBookFromRepo = Optional.ofNullable(bookFromRepoThatIsGoingToBeUpdated)
                .map(Book::getSerieInfo)
                .map(SerieInfo::getSerieId)
                .map(this.seriesRepo::findByIdMandatory);

        // série demandée ?
        Optional<SerieInfo> serieFromRequest = Optional.ofNullable(bookWithUpdatedInformations)
                .map(Book::getSerieInfo)
                .filter(serieInfo -> StringUtils.hasLength(serieInfo.getSerieId()));

        // pas de série dans la requête mais une série présente en base
        // virer la série du book et de la série
        if (serieFromRequest.isEmpty() && serieFromBookFromRepo.isPresent()) {
            bookFromRepoThatIsGoingToBeUpdated.setSerieInfo(null);
            serieFromBookFromRepo
                    .map(serieToUpdate -> serieToUpdate.removeBookFromSerie(bookFromRepoThatIsGoingToBeUpdated))
                    .ifPresent(this.seriesRepo::update);
        }

        // série dans la requete mais pas de série présente en base sur le book a mettre à jour
        // rajouter la série au book et toper la série à mettre à jour
        if (serieFromRequest.isPresent() && serieFromBookFromRepo.isEmpty()) {

            bookFromRepoThatIsGoingToBeUpdated.setSerieInfo(bookWithUpdatedInformations.getSerieInfo());

            serieFromRequest
                    .map(SerieInfo::getSerieId)
                    .map(this.seriesRepo::<Serie>findByIdMandatory)
                    .map(serieFromRepoFromRequest -> serieFromRepoFromRequest.appendBooksToSerie(List.of(bookWithUpdatedInformations)))
                    .ifPresent(this.seriesRepo::update);
        }

        // potentiellement une mise à jour sur deux série (nouvelle et ancienne)
        if (serieFromRequest.isPresent() && serieFromBookFromRepo.isPresent()) {
            serieFromRequest
                    // ce test permet de savoir si les séries diffèrent
                    // si les ids diffèrent, on passera à la suite du traitement des optionals
                    .filter(serieInfoFromRequest -> !serieInfoFromRequest.getSerieId().equals(serieFromBookFromRepo.get().getId()))
                    .map(SerieInfo::getSerieId)
                    .map(this.seriesRepo::<Serie>findByIdMandatory)
                    .ifPresent(serieWhereWeAddABook -> {
                        // association au niveau livre avec la nouvelle série topée en base
                        bookFromRepoThatIsGoingToBeUpdated.associateToSerie(SerieInfo.fromSerie(serieWhereWeAddABook));
                        // rajout dans la série de la requête
                        serieWhereWeAddABook.appendBooksToSerie(List.of(bookFromRepoThatIsGoingToBeUpdated));

                        // suppression dans la série du repo initial
                        Serie serieWhereWeRemoveABook = serieFromBookFromRepo.get();
                        serieWhereWeRemoveABook.removeBookFromSerie(bookFromRepoThatIsGoingToBeUpdated);

                        // sauvegarde des deux séries
                        this.seriesRepo.update(serieWhereWeAddABook);
                        this.seriesRepo.update(serieWhereWeRemoveABook);
                    });

            // dans tous les cas, mise à jour de l'order
            bookFromRepoThatIsGoingToBeUpdated.getSerieInfo().setOrderInSerie(bookWithUpdatedInformations.getSerieInfo().getOrderInSerie());
        }


        // mise à jour des informations de base
        bookFromRepoThatIsGoingToBeUpdated.setAuthor(bookWithUpdatedInformations.getAuthor());
        bookFromRepoThatIsGoingToBeUpdated.setTitle(bookWithUpdatedInformations.getTitle());
        bookFromRepoThatIsGoingToBeUpdated.setSubtitle(bookWithUpdatedInformations.getSubtitle());
        bookFromRepoThatIsGoingToBeUpdated.setOwner(bookWithUpdatedInformations.getOwner());

        // mise à jour en base
        this.booksRepo.update(bookFromRepoThatIsGoingToBeUpdated);



        return true;
    }

}
