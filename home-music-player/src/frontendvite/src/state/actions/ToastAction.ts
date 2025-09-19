import type {MusicPlayerState} from "../MusicPlayerState";
import {ToastMessage} from "../ToastState";
import type {MusicPlayerContextAction} from "./MusicPlayerContextActions";

export class ToastAction implements MusicPlayerContextAction {

    static loading(message: string): ToastAction {
        return new ToastAction("Chargement", message, false);
    }

    static loadingTrack(): ToastAction {
        return ToastAction.loading("Prochain morceau en cours de chargement");
    }

    static autoHide(message: string): ToastAction {
        return new ToastAction("Chargement", message, true);
    }

    static close(): MusicPlayerContextAction {
        return {
            applyToState: (previousState) => {
                return {
                    ...previousState,
                    toastState: {
                        ...previousState.toastState,
                        toastMessage: undefined
                    }
                }
            }
        }

    }

    constructor(private title: string, private message: string, private autoHide: boolean = false) { }



    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        if (this.autoHide) {
            return {
                ...previousState,
                toastState: {
                    ...previousState.toastState,
                    autoHideToastMessage: ToastMessage.info(this.title, this.message)
                }
            }
        } else {
            return {
                ...previousState,
                toastState: {
                    ...previousState.toastState,
                    toastMessage: ToastMessage.info(this.title, this.message)
                }
            }
        }
    }

}