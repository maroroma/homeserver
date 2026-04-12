/* eslint-disable @typescript-eslint/no-explicit-any */
import {Album} from "../model/library/Album";
import {Artist} from "../model/library/Artist";
import {Track} from "../model/library/Track";
import {PlayerStatusEvent} from "../model/player/PlayerStatusEvent";
import {RequesterUtils} from "./RequesterUtils";

export class CreatePlayerRequest {
    constructor(public albumId: string | undefined, public trackId: string, public artistId: string | undefined = undefined) { }

    public static forAlbum(albumId: string, trackId: string): CreatePlayerRequest {
        return new CreatePlayerRequest(albumId, trackId);
    }

    public static forArtist(artistId: string, trackId: string): CreatePlayerRequest {
        return new CreatePlayerRequest(undefined, trackId, artistId);
    }
}

export class AddAlbumToPlayListRequest {
    constructor(public albumId: string | undefined, public artistId: string | undefined = undefined) { }


}


export class PlayerRequester {
    public static startPlayer(album: Album, track: Track): Promise<any> {
        return RequesterUtils.post("/api/musicplayer/player", CreatePlayerRequest.forAlbum(album.id, track.id))
    }
    public static startPlayerForArtist(artist: Artist, track: Track): Promise<any> {
        return RequesterUtils.post("/api/musicplayer/player", CreatePlayerRequest.forArtist(artist.id, track.id))
    }

    public static addAlbumToPlayList(album: Album): Promise<any> {
        return RequesterUtils.put("/api/musicplayer/player/playlist", new AddAlbumToPlayListRequest(album.id))
    }

    public static addAllTracksFromArtistToPlayList(artist: Artist): Promise<any> {
        return RequesterUtils.put("/api/musicplayer/player/playlist", new AddAlbumToPlayListRequest(undefined, artist.id))
    }

    public static stopPlayer(): Promise<any> {
        return RequesterUtils.delete("/api/musicplayer/player");
    }

    public static clearCache():Promise<any> {
        return RequesterUtils.delete("/api/musicplayer/cache");
    }

    public static pausePlayer(): Promise<any> {
        return RequesterUtils.update("/api/musicplayer/player/status/pause");
    }

    public static resumePlayer(): Promise<any> {
        return RequesterUtils.update("/api/musicplayer/player/status/resume");
    }

    public static next(): Promise<any> {
        return RequesterUtils.update("/api/musicplayer/player/playlist/next");
    }

    public static previous(): Promise<any> {
        return RequesterUtils.update("/api/musicplayer/player/playlist/previous");
    }
    public static volumeUp(): Promise<any> {
        return RequesterUtils.update("/api/musicplayer/player/volume/up");
    }
    public static volumeDown(): Promise<any> {
        return RequesterUtils.update("/api/musicplayer/player/volume/down");
    }

    public static setVolume(newVolumeValue: number): Promise<any> {
        return RequesterUtils.update(`/api/musicplayer/player/volume/${newVolumeValue}`);
    }

    public static getFullPlayerStatus():Promise<PlayerStatusEvent> {
        return RequesterUtils.get("/api/musicplayer/player/status/full");
    }
}