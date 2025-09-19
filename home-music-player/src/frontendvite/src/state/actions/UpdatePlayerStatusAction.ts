import type {PlayerStatusEvent} from "../../api/model/player/PlayerStatusEvent";
import type {MusicPlayerState} from "../MusicPlayerState";
import type {MusicPlayerContextAction} from "./MusicPlayerContextActions";
import {ToastAction} from "./ToastAction";

export class UpdatePlayerStatusAction implements MusicPlayerContextAction {
    constructor(private newPlayerStatus: PlayerStatusEvent) { }
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        const toastActionForCurrentSTatus = this.newPlayerStatus.playerStatus === "LOADING" ? ToastAction.loadingTrack() : ToastAction.close();
        return {
            ...toastActionForCurrentSTatus.applyToState(previousState),
            playerStatus: this.newPlayerStatus
        }
    }
}