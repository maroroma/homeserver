import { TrackList } from "../../api/model/embedded/TrackList";
import { PlayerStatusEvent } from "../../api/model/player/PlayerStatusEvent";
import type { MusicPlayerState } from "../MusicPlayerState";
import type { MusicPlayerContextAction } from "./MusicPlayerContextActions";

export class StopEmbeddedPlayerAction implements MusicPlayerContextAction {

    constructor() {
    }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            embeddedPlayerState: {
                ...previousState.embeddedPlayerState,
                trackList: TrackList.empty(),
                currentIndex: -1
            },
            playerStatus: PlayerStatusEvent.empty()
        }
    }

}