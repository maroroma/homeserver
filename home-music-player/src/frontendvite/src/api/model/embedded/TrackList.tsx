import { Comparators } from "../../../tools/Comparators";
import type { Track } from "../library/Track";

export class TrackList {

    public static empty(): TrackList {
        return new TrackList([]);
    }

    public static byTrackName(tracks: Track[]): TrackList {
        return new TrackList(tracks.sort(Comparators.byTrackName()));
    }

    constructor(public tracks: Track[]) { }

    public findIndexOfTrack(trackId: string): number {
        return this.tracks.findIndex(aTrack => aTrack.id === trackId)
    }

    public addTracks(trackListToAdd: TrackList): TrackList {
        return new TrackList([...this.tracks, ...trackListToAdd.tracks])
    }

    public shuffle(): TrackList {
        return new TrackList(this.tracks.sort(Comparators.shuffle()))
    }
}