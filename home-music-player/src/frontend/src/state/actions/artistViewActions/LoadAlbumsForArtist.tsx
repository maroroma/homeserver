import {Album} from "../../../api/model/library/Album";
import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class LoadAlbumsForArtist implements MusicPlayerContextAction {

    constructor(private albumsLoaded:Album[]) {}



    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            artistViewState: {
                ...previousState.artistViewState,
                albums:[...this.albumsLoaded.sort(Sorts.sortByAlbumName())]
            }
        }
    }

}