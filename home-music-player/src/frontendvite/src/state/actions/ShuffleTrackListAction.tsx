import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class ShuffleTrackListAction implements MusicPlayerContextAction {

    constructor() {
    }

    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const trackIdBeforeShuffle = previousState.playerStatus.track.id;

        const shuffleTrackList = previousState.embeddedPlayerState.trackList.shuffle();

        const trackIndexAfterShuffle = shuffleTrackList.findIndexOfTrack(trackIdBeforeShuffle);

        return {
            ...previousState,
            embeddedPlayerState: {
                ...previousState.embeddedPlayerState,
                trackList: shuffleTrackList,
                currentIndex: trackIndexAfterShuffle
            }
        }
    }

}