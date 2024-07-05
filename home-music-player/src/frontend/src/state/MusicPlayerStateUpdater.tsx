import {MusicPlayerState} from "./MusicPlayerState";

export class MusicPlayerStateUpdater {
    public static updateHelloWorld(previousState: MusicPlayerState, newValue: string): MusicPlayerState {
        return {
            ...previousState,
            helloWorld: newValue
        }
    }
}