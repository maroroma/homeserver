import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class CancelAddArtistCandidatesAction implements MusicPlayerContextAction {

    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        return {
            ...previousState,
            addArtistState: {
                ...previousState.addArtistState,
                candidates: []
            },
            viewState: ViewState.AllArtists
        }

    }

}