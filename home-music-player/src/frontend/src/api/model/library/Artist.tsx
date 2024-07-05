import {LibraryItemArts} from "./LibraryItemArts";

export class Artist {
    constructor(public id: string, public name: string, public libraryItemArts: LibraryItemArts, public albums:string[]) { }
}