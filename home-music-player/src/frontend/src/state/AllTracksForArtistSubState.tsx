import {Track} from "../api/model/library/Track"

export type AllTracksForArtistSubState = {
    rawTracks: Track[],
    tracksToDisplay: Track[],
    searchText: string,
    displaySearchBar: boolean
}