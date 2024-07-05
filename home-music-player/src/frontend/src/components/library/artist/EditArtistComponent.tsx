import {FC, useEffect, useState} from "react";
import PopupContainerComponent from "../../common/PopupContainerComponent";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {Form} from "react-bootstrap";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import {LibraryRequester, UpdateArtistRequest} from "../../../api/requesters/LibraryRequester";
import {SaveArtistAction} from "../../../state/actions/artistViewActions/SaveArtistAction";
import {UpdateAllArtistsAction} from "../../../state/actions/UpdateAllArtistsAction";
import {DisplayToastAction} from "../../../state/actions/toastActions/DisplayToastAction";


const EditArtistComponent: FC = () => {

    const { artistViewState, dispatch } = useMusicPlayerContext();

    const [updateRequest, setUpdateRequest] = useState<UpdateArtistRequest>(new UpdateArtistRequest("", true));

    useEffect(() => {
        if (artistViewState.selectedArtist) {
            updateNewName(artistViewState.selectedArtist.name);
        }
    }, [artistViewState]);

    const updateNewName = (newName: string) => {
        setUpdateRequest({
            ...updateRequest,
            newName: newName
        })
    }

    const updateAutoRefresh = (auto: boolean) => {
        setUpdateRequest({
            ...updateRequest,
            autoUpdateArts: auto
        })
    }


    const updateArtist = () => {
        if (artistViewState.selectedArtist) {
            LibraryRequester.updateArtist(artistViewState.selectedArtist, updateRequest)
                .then(updatedArtist => {
                    dispatch(new SaveArtistAction(updatedArtist));
                    return LibraryRequester.getAllArtists();
                }
                )
                .then(updatedArtistList => dispatch(new UpdateAllArtistsAction(updatedArtistList, true)))
                .catch(error => dispatch(DisplayToastAction.error("erreur rencontrées lors de la mise à jour de l'artiste")));

        }

    }

    return <PopupContainerComponent
        title={`Modifier ${artistViewState.selectedArtist?.name}`}
        closeButton="Annuler"
        onCancel={() => dispatch(SimpleViewChangeAction.of(ViewState.Artist))}
        onAccept={() => { updateArtist() }}
        acceptButton="Sauvegarder"
        acceptButtonVariant="primary">

        <Form>
            <Form.Group controlId="formGroupName">
                <Form.Label>Nouveau Nom</Form.Label>
                <Form.Control size="lg" type="text" placeholder={artistViewState.selectedArtist?.name} value={updateRequest.newName} onChange={(event) => updateNewName(event.target.value)} />
            </Form.Group>
            <Form.Group controlId="formGroupName">
                <Form.Label>Illustrations</Form.Label>
                <Form.Check // prettier-ignore
                    type="switch"
                    id="custom-switch"
                    label="Rafraichir les illustrations"
                    className="custom-switch"
                    checked={updateRequest.autoUpdateArts}
                    onChange={event => updateAutoRefresh(event.target.checked)}
                />
            </Form.Group>
        </Form>

    </PopupContainerComponent>

}

export default EditArtistComponent;