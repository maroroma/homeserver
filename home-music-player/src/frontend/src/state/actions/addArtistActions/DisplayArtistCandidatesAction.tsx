import {SimpleFile} from "../../../api/model/files/SimpleFile";
import {Sorts} from "../../../tools/Sorts";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ToastMessage} from "../../ToastState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DisplayArtistCandidatesAction implements MusicPlayerContextAction {

    constructor(private candidates: SimpleFile[]) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        // si pas de candidates, on affiche pas la popup, par contre un petit warning
        if (this.candidates.length === 0) {
            return {
                ...previousState, toastState: {
                    ...previousState.toastState,
                    messages: [ToastMessage.warning("Tous les artistes sont déjà intégrés")]
                }
            };
        } else {
            return {
                ...previousState,
                addArtistState: {
                    ...previousState.addArtistState,
                    candidates: [...this.candidates.sort(Sorts.sortBySimpleFileName())]
                },
                viewState:ViewState.AddArtist
            }
        }
    }

}