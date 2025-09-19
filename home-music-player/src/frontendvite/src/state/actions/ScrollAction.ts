import type {MusicPlayerState} from "../MusicPlayerState";
import type {MusicPlayerContextAction} from "./MusicPlayerContextActions";

export class ScrollAction implements MusicPlayerContextAction {

    constructor(private pageYOffset:number) {}

    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        return {
            ...previousState,
            isScrollOnTop : this.pageYOffset === 0
        }
    }
    
}