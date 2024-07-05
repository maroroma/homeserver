import {Artist} from "../../../api/model/library/Artist";
import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ToastMessage} from "../../ToastState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DeleteArtistAction implements MusicPlayerContextAction {

    constructor(private newArtistList: Artist[]) { }



    applyToState(previousState: MusicPlayerState): MusicPlayerState {


        const sortedArtists = this.newArtistList.sort(Sorts.sortByArtistName());



        return {
            ...previousState,
            artists: [...sortedArtists],
            allArtistsSubState: {
                ...previousState.allArtistsSubState,
                displayedArtists: [...sortedArtists],
            },
            viewState: ViewState.AllArtists,
            toastState: {
                messages: [ToastMessage.info("Artiste supprimé")]
            },
            artistViewState: {
                albums: [],
                selectedArtist: undefined
            }
        }
    }

}