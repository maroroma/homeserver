import AllLogEvents from "../../../model/administration/AllLogEvents";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export class AdministrationLoadedEventsAction implements HomeServerAction {
    constructor(public logEvents: AllLogEvents) { }

    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toastedState = ToastAction.clear().applyToState(previousState);

        return {
            ...toastedState,
            administrationSubState: {
                ...toastedState.administrationSubState,
                allLogEvents: this.logEvents
            },
            workInProgress: false
        }
    }

}