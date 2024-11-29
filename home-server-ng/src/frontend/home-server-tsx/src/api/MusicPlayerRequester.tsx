import MusicPlayerStatus from "../model/musicplayer/MusicPlayerStatus";
import {RequesterUtils} from "./RequesterUtils";

export default class MusicPlayerRequester {
    static getMusicPlayerStatus(): Promise<MusicPlayerStatus> {
        return RequesterUtils.get("/api/music/musicplayer/status");
    }
}