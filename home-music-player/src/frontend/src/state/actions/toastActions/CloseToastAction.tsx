import {MusicPlayerState} from "../../MusicPlayerState";
import {ToastMessage} from "../../ToastState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class CloseToastAction implements MusicPlayerContextAction {

    constructor(private messageToRemove: ToastMessage) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            toastState: {
                ...previousState.toastState,
                messages: [...previousState.toastState.messages.filter(aMessage => aMessage.message !== this.messageToRemove.message)]
            }
        }
    }

}