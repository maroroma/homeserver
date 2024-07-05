import {Album} from "../api/model/library/Album"
import {Artist} from "../api/model/library/Artist"

export type ArtistViewSubState = {
    selectedArtist?: Artist
    albums: Album[]
}