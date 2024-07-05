import {SimpleFile} from "../../../api/model/files/SimpleFile";
import {MusicPlayerState} from "../../MusicPlayerState";
import {ToastMessage} from "../../ToastState";
import {ViewState} from "../../ViewState";
import {MusicPlayerContextAction} from "../MusicPlayerContextActions";

export class DisplayAlbumCandidatesAction implements MusicPlayerContextAction {

    constructor(private candidates: SimpleFile[]) { }


    applyToState(previousState: MusicPlayerState): MusicPlayerState {
        // si pas de candidates, on affiche pas la popup, par contre un petit warning
        if (this.candidates.length === 0) {
            return {
                ...previousState, toastState: {
                    ...previousState.toastState,
                    messages: [ToastMessage.warning("Tous les albums sont déjà intégrés")]
                }
            };
        } else {
            return {
                ...previousState,
                addAlbumSubState: {
                    ...previousState.addAlbumSubState,
                    candidates: [...this.candidates]
                },
                viewState: ViewState.AddAlbum
            }
        }
    }

}