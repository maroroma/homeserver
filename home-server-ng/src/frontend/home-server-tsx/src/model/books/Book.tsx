import SerieInfo from "./SerieInfo";

export default class Book {

    public static empty():Book {
        return new Book("", "", "", [], "", 0, "", new SerieInfo("", "", ""), "", "", "");
    }

    public static sorter() : (b1:Book, b2:Book) => number {
        return (b1, b2) => {
            if (b1.serieInfo !== undefined 
                && b1.serieInfo.orderInSerie !== undefined && b1.serieInfo.orderInSerie !== ""
                && b2.serieInfo !==undefined
                && b2.serieInfo.orderInSerie !== undefined && b2.serieInfo.orderInSerie !== ""
            ) {
                return Number(b1.serieInfo.orderInSerie) - Number(b2.serieInfo.orderInSerie);
            } else {
                return b1.title.toLocaleLowerCase().localeCompare(b2.title.toLocaleLowerCase());
            }
        }
    }

    static bookPicture(aBook:Book):string {
        return `../api/books/${aBook.id}/picture`
    }


    constructor(
        public id: string,
        public title: string,
        public subtitle: string,
        public isbns: string[],
        public author: string,
        public pageCount: number,
        public initialImageLink: string,
        public serieInfo: SerieInfo,
        public pictureFileId: string,
        public owner: string,
        public scrapperSource: string) { }
}