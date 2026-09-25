import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class VolumeDownAction implements MusicPlayerContextAction {

    constructor() {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const nextVolume = previousState.playerStatus.volume - 10 > 0 ? previousState.playerStatus.volume - 10 : 0
        return {
            ...previousState,
            playerStatus: {
                ...previousState.playerStatus,
                volume : nextVolume
            }
        }
    }

}