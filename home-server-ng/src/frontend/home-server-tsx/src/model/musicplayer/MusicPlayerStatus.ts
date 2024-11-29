import {PlayerStatus} from "./PlayerStatus";

export default class MusicPlayerStatus {

    static empty(): MusicPlayerStatus {
        return new MusicPlayerStatus(PlayerStatus.STOPPED, "");
    }

    constructor(public playerStatus: PlayerStatus, public musicPlayerUrl: string) { }
}