import {MusicPlayerState} from "../../MusicPlayerState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export default class UpdateSearchArtistAction implements MusicPlayerContextAction {
    constructor(private newSearchString: string) {

    }
    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        const newDisplayedArtistList = this.newSearchString !== "" ?
            previousState.artists.filter(anArtist => anArtist.name.toLocaleLowerCase().includes(this.newSearchString.toLocaleLowerCase()))
            : [...previousState.artists];


        console.log("UpdateSearchArtistAction", newDisplayedArtistList);

        return {
            ...previousState,
            allArtistsSubState: {
                ...previousState.allArtistsSubState,
                searchText: this.newSearchString,
                displayedArtists: newDisplayedArtistList
            }
        }
    }


}