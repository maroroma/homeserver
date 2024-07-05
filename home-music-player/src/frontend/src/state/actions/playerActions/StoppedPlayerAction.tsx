import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class StoppedPlayerAction implements MusicPlayerContextAction {
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            playerSubState: {
                display: "none",
                lastPlayerStatus: undefined
            },
            viewState: previousState.viewState === ViewState.FullScreenPlayer ? ViewState.AllArtists : previousState.viewState
        }
    }

}