import {Track} from "../../../api/model/library/Track";
import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DisplayAllTracksForArtistAction implements MusicPlayerContextAction {

    constructor(private rawTracks: Track[]) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            allTracksForArtistSubState: {
                ...previousState.allTracksForArtistSubState,
                rawTracks: this.rawTracks,
                tracksToDisplay: this.rawTracks.sort(Sorts.sortByTrackName())
            },
            viewState: ViewState.AllTracksForArtist
        }
    }

}