import type { Album } from "../../api/model/library/Album";
import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class UpdateAlbumOnPlayerStatusAction implements MusicPlayerContextAction {

    constructor(private newAlbum: Album) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            playerStatus: {
                ...previousState.playerStatus,
                album: this.newAlbum
            }
        }
    }

}