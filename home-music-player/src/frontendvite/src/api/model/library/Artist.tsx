import {LibraryItemArts} from "./LibraryItemArts";

export class Artist {

    static empty() : Artist {
        return new Artist("", "", LibraryItemArts.empty(), [])
    }

    constructor(public id: string, public name: string, public libraryItemArts: LibraryItemArts, public albums:string[]) { }
}