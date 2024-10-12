import {HomeServerRootState} from "../states/HomeServerRootState";
import {HomeServerAction} from "./HomeServerAction";
import ToastAction from "./ToastAction";

export default class EndWIPAction implements HomeServerAction {

    constructor(private toastMessage: string = "") { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toastedState = this.toastMessage !== "" ?
            ToastAction.info(this.toastMessage).applyToState(previousState)
            : ToastAction.clear().applyToState(previousState);


        return {
            ...toastedState,
            workInProgress: false
        }
    }

}