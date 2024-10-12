import Task from "../../../model/administration/Task";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export class AdministrationLoadedTasksAction implements HomeServerAction {
    constructor(public tasks: Task[]) { }

    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            administrationSubState: {
                ...previousState.administrationSubState,
                tasks: this.tasks.sort((aProperty1, aProperty2) => aProperty1.id.localeCompare(aProperty2.id))
            },
            workInProgress: false
        }
    }

}