import Serie from "../../../model/books/Serie";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export class SeriesLoadedAction implements HomeServerAction {
    constructor(public series: Serie[]) { }

    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toastedState = ToastAction.clear().applyToState(previousState);

        return {
            ...toastedState,
            workInProgress: false,
            allBooksSubState: {
                ...toastedState.allBooksSubState,
                allSeries: this.series
            }
        }
    }

}