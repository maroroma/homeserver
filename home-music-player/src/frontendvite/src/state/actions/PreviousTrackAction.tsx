import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class PreviousTrackAction implements MusicPlayerContextAction {

    constructor() {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        const newTrackIndex = previousState.embeddedPlayerState.currentIndex > 0 ?
            previousState.embeddedPlayerState.currentIndex - 1
            : previousState.embeddedPlayerState.trackList.tracks.length - 1;
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