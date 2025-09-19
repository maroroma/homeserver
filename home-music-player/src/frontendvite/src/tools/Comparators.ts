import type {Artist} from "../api/model/library/Artist";
import type {Track} from "../api/model/library/Track";

export class Comparators {
    static byArtistsName(): (a1: Artist, a2: Artist) => number {
        return Comparators.by(artist => artist.name);
    }

    static by<T>(extractor: (o1: T) => string): (a1: T, a2: T) => number {
        return (object1, object2) => extractor(object1).toLocaleLowerCase().localeCompare(extractor(object2).toLocaleLowerCase());
    }

    static byTrackName(): (a1: Track, a2: Track) => number {
        return Comparators.by(aTrack => `${aTrack.trackNumber?.padStart(2, "0")} - ${aTrack.name}`)
    }

}