import Book from "./Book";
import Serie from "./Serie";

export default class SerieWithItsBooks {
    static empty(): SerieWithItsBooks {
        return new SerieWithItsBooks(Serie.empty(), []);
    }
    constructor(public serie: Serie, public books: Book[]) { }
}