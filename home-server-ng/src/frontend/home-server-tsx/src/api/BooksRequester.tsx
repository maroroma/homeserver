import Book from "../model/books/Book";
import BooksGroupedBySeries from "../model/books/BooksGroupedBySeries";
import ImportBookProposal from "../model/books/ImportBookProposal";
import ImportBookProposalsForSerieRequest from "../model/books/ImportBookProposalsForSerieRequest";
import ImportFromPageRequest from "../model/books/ImportFromPageRequest";
import SendCollectionsStatusRequest from "../model/books/SendCollectionsStatusRequest";
import Serie from "../model/books/Serie";
import {RequesterUtils} from "./RequesterUtils";

export default class BooksRequester {
    static getAllBooksGroupedBySeries(): Promise<BooksGroupedBySeries> {
        return RequesterUtils.get("../api/books/booksGroupedBySeries");
    }
    static getAllSeries(): Promise<Serie[]> {
        return RequesterUtils.get("../api/books/series");
    }
    static getBooksFromSerie(serie: Serie): Promise<Book[]> {
        return RequesterUtils.get(`../api/books/series/${serie.id}/books`);
    }
    static getSerie(id: string): Promise<Serie> {
        return RequesterUtils.get(`/api/books/series/${id}`);
    }

    static getBooksProposalForSerie(serie: Serie, sanctuaryUrl: string): Promise<ImportBookProposal[]> {
        return RequesterUtils.post("/api/books/import/serieUrl", new ImportFromPageRequest("SANCTUARY", sanctuaryUrl, serie));
    }

    static addBooksToSerieFromProposals(serie: Serie, prefix: string, owner: string, booksToAdd: ImportBookProposal[]): Promise<boolean> {
        return RequesterUtils.post("/api/books/import/bookProposals", new ImportBookProposalsForSerieRequest(serie, prefix, owner, booksToAdd));
    }

    static deleteBook(bookToDelete: Book): Promise<boolean> {
        return RequesterUtils.delete(`/api/books/${bookToDelete.id}`);
    }

    static createSerie(serieTitle: string): Promise<Serie[]> {
        return RequesterUtils.post("/api/books/series", Serie.withTitle(serieTitle));
    }

    static deleteSerie(serieToDelete: Serie): Promise<Serie[]> {
        return RequesterUtils.delete(`/api/books/series/${serieToDelete.id}`);
    }

    static updateSerie(serieToUpdate: Serie): Promise<Serie[]> {
        return RequesterUtils.put("/api/books/series", serieToUpdate);
    }

    static sendEmailForMissingBooks(sendRequest: SendCollectionsStatusRequest): Promise<boolean> {
        return RequesterUtils.post("/api/books/mail/missing", sendRequest);
    }
}