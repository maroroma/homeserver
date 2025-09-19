import {LibraryItemArts} from "./LibraryItemArts";

export class Track {
  static empty(): Track {
    return new Track("", "", LibraryItemArts.empty(), "");
  }
  constructor(
    public id: string,
    public name: string,
    public libraryItemArts: LibraryItemArts,
    public trackNumber: string
  ) {}
}
