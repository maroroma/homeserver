import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class TodoGoBackTargetDirectoriesAction implements HomeServerAction {
    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return { ...previousState }
    }

}