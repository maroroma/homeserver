/* eslint-disable @typescript-eslint/no-explicit-any */
import { Album } from "../model/library/Album";
import { Artist } from "../model/library/Artist";
import { PlayerStatusEvent } from "../model/player/PlayerStatusEvent";
import { PlayList } from "../model/playlists/PlayList";
import { RequesterUtils } from "./RequesterUtils";

export class CreatePlayerRequest {
    constructor(public albumId: string | undefined,
        public artistId: string | undefined = undefined,
        public playListId: string | undefined = undefined,
    ) { }

    public static forAlbum(albumId: string): CreatePlayerRequest {
        return new CreatePlayerRequest(albumId);
    }

    public static forArtist(artistId: string): CreatePlayerRequest {
        return new CreatePlayerRequest(undefined, artistId);
    }

    public static forPlayList(playListId: string): CreatePlayerRequest {
        return new CreatePlayerRequest(undefined, undefined, playListId)
    }
}

export class AddAlbumToPlayListRequest {


    static allTrackFromArtist(artist: Artist): AddAlbumToPlayListRequest {
        return new AddAlbumToPlayListRequest(undefined, artist.id, undefined);
    }

    static allTrackFromalbum(album: Album): AddAlbumToPlayListRequest {
        return new AddAlbumToPlayListRequest(album.id, undefined, undefined);
    }

    static allTrackFromPlayList(playList: PlayList): AddAlbumToPlayListRequest {
        return new AddAlbumToPlayListRequest(undefined, undefined, playList.playListId);
    }


    constructor(public albumId: string | undefined, public artistId: string | undefined = undefined, public playListId: string | undefined) { }


}


export class PlayerRequester {

    public static clearCache(): Promise<any> {
        return RequesterUtils.delete("/api/musicplayer/cache");
    }

    public static getFullPlayerStatus(): Promise<PlayerStatusEvent> {
        return RequesterUtils.get("/api/musicplayer/player/status/full");
    }
}