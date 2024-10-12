import {HomeServerProperty} from "../../../model/administration/HomeServerProperty";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export class AdministrationLoadedPropertiesAction implements HomeServerAction {
    constructor(public properties: HomeServerProperty[]) { }

    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            administrationSubState: {
                ...previousState.administrationSubState,
                properties: this.properties.sort((aProperty1, aProperty2) => aProperty1.id.localeCompare(aProperty2.id))
            },
            workInProgress: false
        }
    }

}