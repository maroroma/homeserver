import {Album} from "../api/model/library/Album"
import {Track} from "../api/model/library/Track"

export type AlbumWithTracksSubState = {
    tracksToDisplay: Track[],
    album?: Album
}