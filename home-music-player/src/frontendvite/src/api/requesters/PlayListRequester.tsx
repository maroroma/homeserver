import type { Track } from "../model/library/Track";
import { PlayList } from "../model/playlists/Playlist";
import { RequesterUtils } from "./RequesterUtils";

export class PlayListRequester {
    public static createNewPlayList(playListName: string): Promise<PlayList> {
        return RequesterUtils.post("/api/musicplayer/playlists", new PlayList("", playListName, []))
    }

    public static addTrackToPlayList(playListId: string, trackId: string): Promise<PlayList> {
        return RequesterUtils.put(`/api/musicplayer/playlists/${playListId}/tracks/${trackId}`, {})
    }

    public static removeTrackFromPlayList(playList: PlayList, trackId: string): Promise<PlayList> {
        return RequesterUtils.delete(`/api/musicplayer/playlists/${playList.playListId}/tracks/${trackId}`);
    }

    public static getAllPlayList(): Promise<PlayList[]> {
        return RequesterUtils.get("/api/musicplayer/playlists");
    }

    public static getOnePlayList(playListId: string): Promise<PlayList> {
        return RequesterUtils.get(`/api/musicplayer/playlists/${playListId}`)
    }

    public static getTracksForPlayList(playList: PlayList): Promise<Track[]> {
        return RequesterUtils.get(`/api/musicplayer/playlists/${playList.playListId}/tracks`)
    }

    public static deletePlayList(playList: PlayList): Promise<any> {
        return RequesterUtils.delete(`/api/musicplayer/playlists/${playList.playListId}`)
    }
}