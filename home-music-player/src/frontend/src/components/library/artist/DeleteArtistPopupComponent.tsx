import {FC} from "react";
import PopupContainerComponent from "../../common/PopupContainerComponent";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {Alert} from "react-bootstrap";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import {DeleteArtistAction} from "../../../state/actions/artistViewActions/DeleteArtistAction";
import {DisplayToastAction} from "../../../state/actions/toastActions/DisplayToastAction";


const DeleteArtistPopupComponent: FC = () => {

    const { artistViewState, dispatch } = useMusicPlayerContext();

    const deleteArtist = () => {
        if (artistViewState.selectedArtist) {
            LibraryRequester.deleteArtist(artistViewState.selectedArtist)
                .then(newArtistList => dispatch(new DeleteArtistAction(newArtistList)))
                .catch(error => dispatch(DisplayToastAction.error("erreur rencontrées lors de la suppression de l'artiste")));

        }
    }

    return <PopupContainerComponent
        title="Supprimer un artiste de la librairie"
        closeButton="Annuler"
        onCancel={() => dispatch(SimpleViewChangeAction.of(ViewState.Artist))}
        onAccept={() => deleteArtist()}
        acceptButton="Supprimer"
        acceptButtonVariant="danger">
        <Alert variant="danger">
            <p>
                <h1>Voulez vous vraiment supprimer l'artiste &lt;{artistViewState.selectedArtist?.name} &gt; ?</h1>
            </p>
        </Alert>
    </PopupContainerComponent>

}

export default DeleteArtistPopupComponent;