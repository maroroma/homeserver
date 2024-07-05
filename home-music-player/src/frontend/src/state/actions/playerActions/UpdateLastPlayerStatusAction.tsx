import {PlayerStatusEvent} from "../../../api/model/player/PlayerStatusEvent";
import {MusicPlayerState} from "../../MusicPlayerState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class UpdateLastPlayerStatusAction implements MusicPlayerContextAction {

    constructor(private lastEvent: PlayerStatusEvent) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            // toastState: {...previousState.toastState, messages : []},
            playerSubState: {
                display: "small",
                lastPlayerStatus: this.lastEvent
            }
        }
    }

}