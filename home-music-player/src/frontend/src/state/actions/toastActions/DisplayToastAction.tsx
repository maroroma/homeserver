import {SimpleBroadcastNotification} from "../../../api/model/notifications/SimpleBroadcastNotification";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ToastMessage, ToastType} from "../../ToastState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DisplayToastAction implements MusicPlayerContextAction {

    constructor(private message: string, private type: ToastType, private persistent: boolean = false) { }

    public static info(message: string, persistent: boolean = false): DisplayToastAction {
        return new DisplayToastAction(message, "Info", persistent);
    }
    public static error(message: string): DisplayToastAction {
        return new DisplayToastAction(message, "Error");
    }

    public static fromNotication(notitication: SimpleBroadcastNotification): DisplayToastAction {
        if (notitication.level === "ERROR") {
            return DisplayToastAction.error(notitication.message);
        }

        return DisplayToastAction.info(notitication.message, notitication.persistent);
    }




    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const canAddNewMessage = !previousState.toastState.messages.some(aToastMessage => aToastMessage.message === this.message)

        return {
            ...previousState,
            toastState: {
                ...previousState.toastState,
                messages: canAddNewMessage ? [...previousState.toastState.messages, new ToastMessage(this.message, this.type, this.persistent)] : previousState.toastState.messages
            }
        }
    }

}