import {Artist} from "../../../api/model/library/Artist";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class SaveArtistAction implements MusicPlayerContextAction {

    constructor(private updatedArtist: Artist) { }



    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            artistViewState: {
                ...previousState.artistViewState,
                selectedArtist: this.updatedArtist
            },
            viewState: ViewState.Artist
        }
    }
}