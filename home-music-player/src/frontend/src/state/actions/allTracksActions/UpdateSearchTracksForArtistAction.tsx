import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class UpdateSearchTracksForArtistAction implements MusicPlayerContextAction {

    constructor(private newSearchString: string) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            allTracksForArtistSubState: {
                ...previousState.allTracksForArtistSubState,
                searchText: this.newSearchString,
                tracksToDisplay: previousState.allTracksForArtistSubState.rawTracks
                    .filter(aTrack => aTrack.name.toLocaleLowerCase().includes(this.newSearchString.toLocaleLowerCase()))
                    .sort(Sorts.sortByTrackName())
            }
        }
    }

}