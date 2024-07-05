import {LibraryItemArts} from "./LibraryItemArts";

export class Track {
    constructor(public id: string, public name: string, public libraryItemArts: LibraryItemArts, public trackNumber:string) { }
}