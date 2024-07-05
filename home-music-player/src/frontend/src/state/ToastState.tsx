import {SimpleBroadcastNotification} from "../api/model/notifications/SimpleBroadcastNotification";


export type ToastType = "Info" | "Warning" | "Error" | "InProgress";

export class ToastMessage {
    constructor(public message: string, public type: ToastType, public persistent: boolean = false) { }

    public static info(message: string): ToastMessage {
        return new ToastMessage(message, "Info");
    }

    public static warning(message: string): ToastMessage {
        return new ToastMessage(message, "Warning");
    }

    public static fromNotification(notification:SimpleBroadcastNotification):ToastMessage {
        if (notification.level === "ERROR") {
            return new ToastMessage(notification.message, "Error");
        }

        return ToastMessage.info(notification.message);
    }
}

export type ToastState = {
    messages: ToastMessage[]
}
