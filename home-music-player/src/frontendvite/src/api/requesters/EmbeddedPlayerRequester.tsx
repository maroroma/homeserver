import type { TrackList } from "../model/embedded/TrackList";
import type { Album } from "../model/library/Album";
import type { Artist } from "../model/library/Artist";
import type { Track } from "../model/library/Track";
import type { PlayList } from "../model/playlists/PlayList";
import { CreatePlayerRequest } from "./PlayerRequester";
import { RequesterUtils } from "./RequesterUtils";

export class EmbeddedPlayerRequester {
    public static generareTrackListFromAlbum(album: Album): Promise<TrackList> {
        return RequesterUtils.post("/api/musicplayer/embedded/tracklist", CreatePlayerRequest.forAlbum(album.id))
    }

    public static generareTrackListFromArtist(artist: Artist): Promise<any> {
        return RequesterUtils.post("/api/musicplayer/embedded/tracklist", CreatePlayerRequest.forArtist(artist.id))
    }

    public static generareTrackListFromPlayList(playList: PlayList): Promise<any> {
        return RequesterUtils.post("/api/musicplayer/embedded/tracklist", CreatePlayerRequest.forPlayList(playList.playListId))
    }

    public static trackDownloadUrl(aTrack: Track): string {
        return `api/musicplayer/embedded/track/${aTrack.id}/stream`
    }
}