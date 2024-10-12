import Book from "./Book";
import Serie from "./Serie";

export default class SerieWithFullBooks {

    static empty(): SerieWithFullBooks {
        return new SerieWithFullBooks("", "", [])
    }

    static seriePicture(serie: Serie): string {
        return "../api/books/series/" + serie.id + "/picture"
    }


    constructor(public id: string, public title: string, public books: Book[]) {
    }

}