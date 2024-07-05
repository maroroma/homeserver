import {Album} from "../../../api/model/library/Album";
import {Track} from "../../../api/model/library/Track";
import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DisplayAlbumAction implements MusicPlayerContextAction {

    constructor(private selectedAlbum: Album, private tracksToDisplay: Track[]) { }



    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            viewState: ViewState.AlbumWithTracks,
            albumWithTracksSubState: {
                album: this.selectedAlbum,
                tracksToDisplay: this.tracksToDisplay.sort(Sorts.sortByTrackName())
            }
        }
    }

}