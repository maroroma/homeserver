import {HomeServerRootState} from "../states/HomeServerRootState";
import {HomeServerAction} from "./HomeServerAction";

export default class UpdateSearchStringAction implements HomeServerAction {

    static clear(): UpdateSearchStringAction {
        return new UpdateSearchStringAction("");
    }

    constructor(private newSearchString: string) { }
    applyToState(previousState: HomeServerRootState): HomeServerRootState {
        return {
            ...previousState,
            searchString: this.newSearchString
        }
    }

}