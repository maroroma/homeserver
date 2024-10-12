import BooksGroupedBySeries from "../../model/books/BooksGroupedBySeries"
import Serie from "../../model/books/Serie"
import SerieWithItsBooks from "../../model/books/SerieWithItsBooks"

export type AllBooksSubState = {
    booksGroupedBySeries: BooksGroupedBySeries,
    allSeries: Serie[],
    selectedSerie: SerieWithItsBooks
}