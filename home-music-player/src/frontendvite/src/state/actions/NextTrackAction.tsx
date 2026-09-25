import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class NextTrackAction implements MusicPlayerContextAction {

    constructor() {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        const newTrackIndex = previousState.embeddedPlayerState.currentIndex + 1 < previousState.embeddedPlayerState.trackList.tracks.length ?
            previousState.embeddedPlayerState.currentIndex + 1
            : 0;
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