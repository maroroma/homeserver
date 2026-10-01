import type { Album } from "../library/Album";
import type { Artist } from "../library/Artist";
import type { Track } from "../library/Track";

export class SearchResponse {

    public static empty(): SearchResponse {
        return new SearchResponse([], [], []);
    }

    constructor(public tracks: Track[], public albums: Album[], public artists: Artist[]) { }
}