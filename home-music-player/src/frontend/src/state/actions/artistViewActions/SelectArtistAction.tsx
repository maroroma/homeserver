import {Artist} from "../../../api/model/library/Artist";
import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class SelectArtistAction implements MusicPlayerContextAction {

    constructor(private selectedArtist: Artist) { }

    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            artistViewState: {
                ...previousState.artistViewState,
                selectedArtist: this.selectedArtist,
            },
            allArtistsSubState: {
                ...previousState.allArtistsSubState,
                displaySearchBar: false,
                displayedArtists: [...previousState.artists.sort(Sorts.sortByArtistName())],
                searchText: ""
            },
            viewState: ViewState.Artist
        }
    }

}