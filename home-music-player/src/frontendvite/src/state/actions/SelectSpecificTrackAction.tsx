import type { Track } from "../../api/model/library/Track";
import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class SelectSpecificTrackAction implements MusicPlayerContextAction {

    constructor(private selectedTrack: Track) {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        const newTrackIndex = previousState.embeddedPlayerState.trackList.findIndexOfTrack(this.selectedTrack.id)
        return {
            ...previousState,
            embeddedPlayerState: {
                ...previousState.embeddedPlayerState,
                currentIndex: newTrackIndex
            },
            playerStatus: {
                ...previousState.playerStatus,
                track: previousState.embeddedPlayerState.trackList.tracks[newTrackIndex]
            }
        }
    }

}