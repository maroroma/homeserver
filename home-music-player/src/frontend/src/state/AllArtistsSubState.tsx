import {Artist} from "../api/model/library/Artist"


export type AllArtistsSubState = {
    displaySearchBar: boolean
    searchText: string;
    displayedArtists: Artist[];
}