import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class LeaveFullScreenAction implements MusicPlayerContextAction {
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            playerSubState: {
                ...previousState.playerSubState,
                display: "small"
            },
            viewState: ViewState.AllArtists
        }
    }

}