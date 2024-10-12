import {HomeServerRootState} from "../states/HomeServerRootState";
import {HomeServerAction} from "./HomeServerAction";
import ToastAction from "./ToastAction";

export default class EndWIPInErrorAction implements HomeServerAction {


    constructor(private errorMessage: string) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toastedState = ToastAction.error(this.errorMessage).applyToState(previousState);


        return {
            ...toastedState,
            workInProgress: false
        }
    }

}