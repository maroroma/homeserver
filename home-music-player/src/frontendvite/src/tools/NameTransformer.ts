import type { Album } from "../api/model/library/Album";
import type { Artist } from "../api/model/library/Artist";
import type { Track } from "../api/model/library/Track";

export class NameTransformer {
    static trackName(aTrack: Track): string {
        return aTrack.name
            .replace(".mp3", "")
            .replace(".MP3", "");
    }

    static albumName(anAlbum: Album, anArtist: Artist): string {
        return anAlbum.name
            .replace(anArtist.name + " - ", "")
            .replace(anArtist.name.toUpperCase() + " - ", "")
            .replace(anArtist.name.toLowerCase() + " - ", "")
            ;
    }
}