import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DisplayFullscreenPlayerAction implements MusicPlayerContextAction {
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            playerSubState: {
                ...previousState.playerSubState,
                display: "fullscreen"
            },
            viewState: ViewState.FullScreenPlayer
        }
    }

}