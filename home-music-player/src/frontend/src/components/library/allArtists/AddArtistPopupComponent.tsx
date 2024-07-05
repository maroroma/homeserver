import {FC} from "react";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import PopupContainerComponent from "../../common/PopupContainerComponent";
import {ListGroup, ListGroupItem} from "react-bootstrap";

import "./AddArtistPopupComponent.css";
import {CancelAddArtistCandidatesAction} from "../../../state/actions/addArtistActions/CancelAddArtistCandidatesAction";
import {SimpleFile} from "../../../api/model/files/SimpleFile";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import {UpdateAllArtistsAction} from "../../../state/actions/UpdateAllArtistsAction";
import {PersonAdd} from "react-bootstrap-icons";

const AddArtistPopupComponent: FC = () => {

    const { addArtistState, dispatch } = useMusicPlayerContext();

    const launchArtistScan = (selectedCandidate: SimpleFile) => {
        LibraryRequester.createArtist(selectedCandidate).then(response => dispatch(new UpdateAllArtistsAction(response)));
    }


    return <PopupContainerComponent
        title="Sélectionnez un artiste à scanner"
        onCancel={() => dispatch(new CancelAddArtistCandidatesAction())}
        displayApplyButton={false}
    >
        <ListGroup>
            {addArtistState.candidates.map((aCandidate, candidateIndex) =>
                <ListGroupItem action key={candidateIndex} onClick={() => launchArtistScan(aCandidate)}><h2><PersonAdd className="add-artist-add-icon" />{aCandidate.name}</h2></ListGroupItem>
            )}
        </ListGroup>
    </PopupContainerComponent>

}

export default AddArtistPopupComponent;
