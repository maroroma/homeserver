import Book from "../../../model/books/Book";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";
import ToastAction from "../ToastAction";

export default class SelectedSerieBooksLoadedAction implements HomeServerAction {


    constructor(private books: Book[]) { }


    applyToState(previousState: HomeServerRootState): HomeServerRootState {

        const toastedState = ToastAction.clear().applyToState(previousState);

        return {
            ...toastedState,
            allBooksSubState: {
                ...toastedState.allBooksSubState,
                selectedSerie: {
                    ...toastedState.allBooksSubState.selectedSerie,
                    books: this.books
                }
            },
            workInProgress: false
        }
    }

}