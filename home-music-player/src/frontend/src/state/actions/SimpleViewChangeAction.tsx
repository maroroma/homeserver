import {MusicPlayerState} from "../MusicPlayerState";
import {ViewState} from "../ViewState";
import {MusicPlayerContextAction} from "./MusicPlayerContextActions";

export default class SimpleViewChangeAction implements MusicPlayerContextAction {
    constructor(private view: ViewState) { }


    public static of(view:ViewState): SimpleViewChangeAction {
        return new SimpleViewChangeAction(view);
    }

    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            viewState: this.view
        }
    }

}