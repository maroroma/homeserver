import type {Track} from "../api/model/library/Track";

export class NameTransformer {
    static trackName(aTrack: Track): string {
        return aTrack.name.replace(".mp3", "");
    }
}