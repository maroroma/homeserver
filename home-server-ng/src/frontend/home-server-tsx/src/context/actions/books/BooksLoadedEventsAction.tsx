import BooksGroupedBySeries from "../../../model/books/BooksGroupedBySeries";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export class BooksLoadedEventsAction implements HomeServerAction {
    constructor(public booksGroupedBySeries: BooksGroupedBySeries) { }

    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        var toastedState = ToastAction.clear().applyToState(previousState);

        return {
            ...toastedState,
            workInProgress: false,
            allBooksSubState: {
                ...toastedState.allBooksSubState,
                booksGroupedBySeries: this.booksGroupedBySeries
            }
        }
    }

}