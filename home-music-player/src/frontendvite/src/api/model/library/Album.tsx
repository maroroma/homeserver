import {LibraryItemArts} from "./LibraryItemArts";

export class Album {

    static empty() : Album {
        return new Album("", "", LibraryItemArts.empty())
    }

    constructor(public id: string, public name: string, public libraryItemArts: LibraryItemArts) { }
}