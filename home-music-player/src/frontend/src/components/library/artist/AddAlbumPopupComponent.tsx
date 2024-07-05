import {FC} from "react";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import PopupContainerComponent from "../../common/PopupContainerComponent";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import {ListGroup, ListGroupItem} from "react-bootstrap";
import {FilePlus} from "react-bootstrap-icons";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import {SimpleFile} from "../../../api/model/files/SimpleFile";
import {SelectArtistAction} from "../../../state/actions/artistViewActions/SelectArtistAction";
import {UpdateAllArtistsAction} from "../../../state/actions/UpdateAllArtistsAction";

const AddAlbumPopupComponent: FC = () => {
    const { artistViewState, addAlbumSubState, dispatch } = useMusicPlayerContext();


    const addAlbum = (selectedAlbumToAdd: SimpleFile) => {
        if (artistViewState.selectedArtist) {
            LibraryRequester.addAlbumToArtist(selectedAlbumToAdd, artistViewState.selectedArtist)
                .then(updatedArtist => {
                    dispatch(new SelectArtistAction(updatedArtist))
                    return LibraryRequester.getAllArtists();
                } )
                .then(updatedArtistList => dispatch(new UpdateAllArtistsAction(updatedArtistList, true)));
        }
    }



    return <PopupContainerComponent
        title="Sélectionner un album à ajouter"
        onCancel={() => dispatch(SimpleViewChangeAction.of(ViewState.Artist))}
        displayApplyButton={false}
    >


        <ListGroup>
            {addAlbumSubState.candidates.map((aCandidate, candidateIndex) =>
                <ListGroupItem action key={candidateIndex} onClick={() => { addAlbum(aCandidate) }}><h2><FilePlus className="add-artist-add-icon" />{aCandidate.name}</h2></ListGroupItem>
            )}
        </ListGroup>
    </PopupContainerComponent>


}

export default AddAlbumPopupComponent;