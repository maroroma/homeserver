import { useEffect, useState, type FC } from "react";
import FadeInPage from "../FadeInPage";
import { useAlbumProject, useArtist, useCustomNavigate } from "../hooks/CustomHooks";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { ConeStriped } from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import ImageUploadComponent from "../uploaders/ImageUploadComponent";
import CssTools from "../../tools/CssTools";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { Form } from "react-bootstrap";
import MenuItemApplyComponent from "../menu/MenuItemApplyComponent";
import { ToastAction } from "../../state/actions/ToastAction";
import { AlbumProjectRequester } from "../../api/requesters/AlbumProjectRequester";
import { CreateAlbumProjectRequest } from "../../api/model/albumproject/CreateAlbumProjectRequest";
import FilesUploadComponent from "../uploaders/FilesUploadComponent";

const AddTracksToAlbumProjectComponent: FC = () => {

    const navigate = useCustomNavigate();
    const { dispatch } = useMusicPlayerContext();
    const { albumProject } = useAlbumProject();


    const [albumNameValid, setAlbumNameValid] = useState(false);
    const [albumArtBase64File, setAlbumArtBase64File] = useState("")

    return (
        <FadeInPage
            label={`Uploader les tracks à l'album ${albumProject.albumName}`}
            icon={<ConeStriped />}
            onClick={() => navigate(Paths.ONE_ARTIST.resolve(albumProject.artistId))}
        >
            <div className={CssTools.of().defaultPadding().css()}>
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                    <Form.Label>Nom de l'album</Form.Label>
                    <Form.Control type="text"
                        value={albumProject.albumName}
                        disabled
                    />
                </Form.Group>
                <FilesUploadComponent title="Sélectionnez les fichiers"/>
            </div>

            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.ONE_ARTIST.resolve(albumProject.artistId))}
                />
                <MenuItemApplyComponent
                    valid={albumNameValid && albumArtBase64File !== ""}
                />
            </MenuComponent>
        </FadeInPage>
    );
}

export default AddTracksToAlbumProjectComponent;