export default class Serie {

    static sorter(): (s1: Serie, s2: Serie) => number {
        return (s1, s2) => {
            return s1.title.toLocaleLowerCase().localeCompare(s2.title.toLocaleLowerCase());
        }
    }

    static filter(searchString: string): (serie: Serie) => boolean {
        return (aSerie) => aSerie.title.toLocaleLowerCase().includes(searchString.toLocaleLowerCase());
    }

    static equalsByTitle(otherTitle: string): (serie: Serie) => boolean {
        return aSerie => aSerie.title === otherTitle;
    }

    static empty(): Serie {
        return new Serie("", "", [], "", false, "");
    }

    static withTitle(title: string): Serie {
        const serie = Serie.empty();
        serie.title = title;
        return serie;
    }

    static seriePicture(serie: Serie): string {
        return "/api/books/series/" + serie.id + "/picture"
    }



    constructor(public id: string, public title: string, public bookIds: string[], public pictureFileId: string, public completed: boolean, public serieUrlForImport: string) { }
}