export class LibraryItemArts {

    static empty() : LibraryItemArts {
        return new LibraryItemArts(null, null);
    }

    constructor(public thumbPath: string|null, public fanartPath: string|null) { }
}