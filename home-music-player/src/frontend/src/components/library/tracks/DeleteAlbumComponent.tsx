import {FC} from "react";
import PopupContainerComponent from "../../common/PopupContainerComponent";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {Alert} from "react-bootstrap";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import {SelectArtistAction} from "../../../state/actions/artistViewActions/SelectArtistAction";
import {UpdateAllArtistsAction} from "../../../state/actions/UpdateAllArtistsAction";


const DeleteAlbumComponent: FC = () => {

    const { artistViewState, albumWithTracksSubState: allTracksSubState,  dispatch } = useMusicPlayerContext();

    const deleteAlbum = () => {
        if (artistViewState.selectedArtist && allTracksSubState.album) {
            LibraryRequester.deleteAlbum(artistViewState.selectedArtist, allTracksSubState.album)
                .then(updatedArtist => {
                    dispatch(new SelectArtistAction(updatedArtist))   
                    return LibraryRequester.getAllArtists();
                } )
                .then(updatedArtistList => dispatch(new UpdateAllArtistsAction(updatedArtistList, true)));
        }
    }

    return <PopupContainerComponent
        title="Supprimer un album de la librairie"
        closeButton="Annuler"
        onCancel={() => dispatch(SimpleViewChangeAction.of(ViewState.AlbumWithTracks))}
        onAccept={() => deleteAlbum()}
        acceptButton="Supprimer"
        acceptButtonVariant="danger">
        <Alert variant="danger">
            <p>
                <h1>Voulez vous vraiment supprimer l'album &lt;{allTracksSubState.album?.name} &gt; de l'artiste &lt;{artistViewState.selectedArtist?.name} &gt; ?</h1>
            </p>
        </Alert>
    </PopupContainerComponent>

}

export default DeleteAlbumComponent;