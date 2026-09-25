import { TrackList } from "../../api/model/embedded/TrackList";
import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class AddTracksToTrackListAction implements MusicPlayerContextAction {

    constructor(private newTrackList: TrackList) {

    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const sortedTrackList = TrackList.byTrackName(this.newTrackList.tracks);
        
        return {
            ...previousState,
            embeddedPlayerState: {
                ...previousState.embeddedPlayerState,
                trackList: previousState.embeddedPlayerState.trackList.addTracks(sortedTrackList),
            }
        }
    }

}