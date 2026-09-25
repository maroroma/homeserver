import type { TrackList } from "../api/model/embedded/TrackList"

export type EmbeddedPlayerState = {
    trackList: TrackList,
    currentIndex: number
}