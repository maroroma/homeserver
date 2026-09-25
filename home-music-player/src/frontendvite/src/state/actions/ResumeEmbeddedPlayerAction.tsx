import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class ResumeEmbeddedPlayerAction implements MusicPlayerContextAction {

    constructor() {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            playerStatus: {
                ...previousState.playerStatus,
                playerStatus : "PLAYING"
            }
        }
    }

}