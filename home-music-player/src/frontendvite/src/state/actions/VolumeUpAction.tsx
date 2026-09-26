import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class VolumeUpAction implements MusicPlayerContextAction {

    constructor() {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const nextVolume = previousState.playerStatus.volume + 10 < 100 ? previousState.playerStatus.volume + 10 : 100


        return {
            ...previousState,
            playerStatus: {
                ...previousState.playerStatus,
                volume : nextVolume
            }
        }
    }

}