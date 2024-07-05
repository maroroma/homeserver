import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class GoBackToAllArtistsAction implements MusicPlayerContextAction {
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            artistViewState: {
                ...previousState.artistViewState,
                albums: []
            },
            viewState: ViewState.AllArtists,
        }
    }

}