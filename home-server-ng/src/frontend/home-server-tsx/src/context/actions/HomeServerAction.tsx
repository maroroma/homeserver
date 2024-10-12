import {HomeServerRootState} from "../states/HomeServerRootState";

export interface HomeServerAction {
    applyToState(previousState:HomeServerRootState) : HomeServerRootState;
}