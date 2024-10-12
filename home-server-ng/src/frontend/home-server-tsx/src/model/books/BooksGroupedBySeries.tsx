import SerieWithFullBooks from "./SerieWithFullBooks";

export default class BooksGroupedBySeries {

    static empty(): BooksGroupedBySeries {
        return new BooksGroupedBySeries([], SerieWithFullBooks.empty());
    }


    constructor(public booksWithSerie: SerieWithFullBooks[],
        public booksWithoutSerie: SerieWithFullBooks) { }
}