import type { Artist } from "../../api/model/library/Artist";
import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class UpdateArtistOnPlayerStatusAction implements MusicPlayerContextAction {

    constructor(private newArtist: Artist) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            playerStatus: {
                ...previousState.playerStatus,
                artist: this.newArtist
            }
        }
    }

}