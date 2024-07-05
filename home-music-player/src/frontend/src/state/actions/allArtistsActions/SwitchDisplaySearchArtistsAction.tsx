import {MusicPlayerState} from "../../MusicPlayerState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class SwitchDisplaySearchArtistsAction implements MusicPlayerContextAction {


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            allArtistsSubState: {
                ...previousState.allArtistsSubState,
                searchText: "",
                displayedArtists: [...previousState.artists],
                displaySearchBar: !previousState.allArtistsSubState.displaySearchBar
            }
        }
    }

}