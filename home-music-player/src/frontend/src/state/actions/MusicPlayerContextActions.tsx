import {MusicPlayerState} from "../MusicPlayerState";

export interface MusicPlayerContextAction {
    applyToState(previousState: MusicPlayerState): MusicPlayerState
}
