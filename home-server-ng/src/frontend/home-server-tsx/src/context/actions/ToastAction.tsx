import {BootstrapVariants} from "../../components/bootstrap/BootstrapVariants";
import {HomeServerRootState} from "../states/HomeServerRootState";
import {HomeServerAction} from "./HomeServerAction";

export default class ToastAction implements HomeServerAction {


    static info(toastMessage: string): ToastAction {
        return new ToastAction(toastMessage, BootstrapVariants.Info)
    }

    static clear(): ToastAction {
        return new ToastAction("", BootstrapVariants.Info);
    }

    static error(errorMessage: string): ToastAction {
        return new ToastAction(errorMessage, BootstrapVariants.Danger);
    }


    constructor(private toastMessage: string, private variant: BootstrapVariants) { }




    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            toastSubState: {
                ...previousState.toastSubState,
                toastMessage: this.toastMessage,
                variant: this.variant
            }
        }
    }

}