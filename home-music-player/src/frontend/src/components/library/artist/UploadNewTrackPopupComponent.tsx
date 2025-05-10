import {FC, useState} from "react";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import PopupContainerComponent from "../../common/PopupContainerComponent";
import SimpleViewChangeAction from "../../../state/actions/SimpleViewChangeAction";
import {ViewState} from "../../../state/ViewState";
import {Form} from "react-bootstrap";
import {LibraryRequester} from "../../../api/requesters/LibraryRequester";
import {DisplayAlbumAction} from "../../../state/actions/allTracksActions/DisplayAlbumAction";

const UploadNewTrackPopupComponent: FC = () => {
  const { artistViewState, albumWithTracksSubState, dispatch } =
    useMusicPlayerContext();

    const [filesToUpload, setFilesToUpload] = useState<File[]>([]);

    // const [fileToUpload]

    const uploadFileToAlbum = () => {
        if (artistViewState.selectedArtist && albumWithTracksSubState.album) {
            LibraryRequester.addNewTrackToAlbum(albumWithTracksSubState.album, filesToUpload[0])
            .then(response => {
                dispatch(new DisplayAlbumAction(albumWithTracksSubState.album!, response))
            });
        }
    }

  return (
    <PopupContainerComponent
      title="Sélectionner un fichier à rajouter"
      onCancel={() =>
        dispatch(SimpleViewChangeAction.of(ViewState.AlbumWithTracks))
      }
      onAccept={() => uploadFileToAlbum()}
      acceptButton="uploader"
    >
      <Form.Group controlId="formFileMultiple" className="mb-3">
        <Form.Label>Sélectionner les fichier</Form.Label>
        <Form.Control
          type="file"
          onChange={(event: any) => 
            setFilesToUpload(Array.from(event.target.files))
          }
        />

      </Form.Group>
     
    </PopupContainerComponent>
  );
};

export default UploadNewTrackPopupComponent;
