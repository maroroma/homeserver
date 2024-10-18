import {HomeServerAction} from "../actions/HomeServerAction";
import {AdministrationSubState} from "./AdministrationSubState";
import {AllBooksSubState} from "./AllBooksSubState";
import {FileManagerSubState} from "./FileManagerSubState";
import {SeedboxTodoSubState} from "./SeedboxTodoSubState";
import {ToastSubState} from "./ToastSubState";


export type HomeServerRootState = {
    dispatch: React.Dispatch<HomeServerAction>;
    helloWorld: string,
    administrationSubState: AdministrationSubState,
    workInProgress: boolean,
    searchString: string,
    toastSubState: ToastSubState,
    allBooksSubState: AllBooksSubState,
    fileManagerSubState: FileManagerSubState,
    seedboxTodoSubState: SeedboxTodoSubState

}