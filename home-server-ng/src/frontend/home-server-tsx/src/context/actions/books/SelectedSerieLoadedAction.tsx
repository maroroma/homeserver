import Serie from "../../../model/books/Serie";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class SelectedSerieLoadedAction implements HomeServerAction {


    constructor(private selectedSerie: Serie) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        const toastedState = ToastAction.clear().applyToState(previousState);

        return {
            ...toastedState,
            allBooksSubState: {
                ...toastedState.allBooksSubState,
                selectedSerie: {
                    ...toastedState.allBooksSubState.selectedSerie,
                    serie: this.selectedSerie
                }
            },
            workInProgress: false
        }
    }

}