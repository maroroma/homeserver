import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class SwitchSearchBarAllTracksForArtistAction implements MusicPlayerContextAction {
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            allTracksForArtistSubState: {
                ...previousState.allTracksForArtistSubState,
                displaySearchBar: !previousState.allTracksForArtistSubState.displaySearchBar,
                searchText: "",
                tracksToDisplay: [...previousState.allTracksForArtistSubState.rawTracks.sort(Sorts.sortByTrackName())]
            }
        }
    }

}