import {MusicPlayerContextAction} from "./MusicPlayerContextActions";
import {MusicPlayerState} from "../MusicPlayerState";

export class UpdateHelloWorldAction implements MusicPlayerContextAction {
    constructor(private newValue: string) { }
    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            helloWorld: this.newValue
        }
    }

}