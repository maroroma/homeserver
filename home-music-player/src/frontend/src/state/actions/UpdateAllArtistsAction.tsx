import {Artist} from "../../api/model/library/Artist";
import {Sorts} from "../../tools/Sorts";
import {MusicPlayerState} from "../MusicPlayerState";
import {ToastMessage} from "../ToastState";
import {ViewState} from "../ViewState";
import {MusicPlayerContextAction} from "./MusicPlayerContextActions";

export class UpdateAllArtistsAction implements MusicPlayerContextAction {

    constructor(private artists: Artist[], private backgroundUpdate: boolean = false) { }

    applyToState(previousState: MusicPlayerState): MusicPlayerState {

        console.log("UpdateAllArtistsAction", this.backgroundUpdate, this.artists);


        const sortedArtists = this.artists.sort(Sorts.sortByArtistName());
        // const sortedArtists = this.artists.sort((a1, a2) => a1.name.toLocaleLowerCase().localeCompare(a2.name.toLocaleLowerCase()));

        return {
            ...previousState,
            artists: sortedArtists,
            allArtistsSubState: {
                ...previousState.allArtistsSubState,
                displayedArtists: sortedArtists,
                searchText: "",
                displaySearchBar: false,
            },
            viewState: this.backgroundUpdate ? previousState.viewState : ViewState.AllArtists,
            toastState: {
                ...previousState.toastState,
                messages: [ToastMessage.info("Chargement des artistes terminé")]
            }
        }
    }

}