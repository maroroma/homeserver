import type { AlbumProject } from "../model/albumproject/AlbumProject";
import type { CreateAlbumProjectRequest } from "../model/albumproject/CreateAlbumProjectRequest";
import { RequesterUtils } from "./RequesterUtils";

export class AlbumProjectRequester {
    public static createProject(projectRequest: CreateAlbumProjectRequest): Promise<AlbumProject> {
        return RequesterUtils.post("/api/musicplayer/library/albums/projects", projectRequest);
    }

    public static getOneProject(projectId: string): Promise<AlbumProject> {
        return RequesterUtils.get(`/api/musicplayer/library/albums/projects/${projectId}`)
    }


    // public static addTrackToPlayList(playListId: string, trackId: string): Promise<PlayList> {
    //     return RequesterUtils.put(`/api/musicplayer/playlists/${playListId}/tracks/${trackId}`, {})
    // }

    // public static removeTrackFromPlayList(playList: PlayList, trackId: string): Promise<PlayList> {
    //     return RequesterUtils.delete(`/api/musicplayer/playlists/${playList.playListId}/tracks/${trackId}`);
    // }

    // public static getAllPlayList(): Promise<PlayList[]> {
    //     return RequesterUtils.get("/api/musicplayer/playlists");
    // }



    // public static getTracksForPlayList(playList: PlayList): Promise<Track[]> {
    //     return RequesterUtils.get(`/api/musicplayer/playlists/${playList.playListId}/tracks`)
    // }

    // public static deletePlayList(playList: PlayList): Promise<any> {
    //     return RequesterUtils.delete(`/api/musicplayer/playlists/${playList.playListId}`)
    // }
}