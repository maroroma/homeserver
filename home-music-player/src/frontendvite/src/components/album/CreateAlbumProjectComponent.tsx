import { useEffect, useState, type FC } from "react";
import FadeInPage from "../FadeInPage";
import { useArtist, useCustomNavigate } from "../hooks/CustomHooks";
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

const CreateAlbumProjectComponent: FC = () => {

    const navigate = useCustomNavigate();
    const { dispatch } = useMusicPlayerContext();

    const { artist } = useArtist();

    const [albumNameValid, setAlbumNameValid] = useState(false);
    const [albumName, setAlbumName] = useState("");
    const [albumArtBase64File, setAlbumArtBase64File] = useState("")


    useEffect(() => {
        setAlbumNameValid(albumName !== "");
    }, [albumName]);


    const createProjectAndGoToAddTracks = () => {

        dispatch(ToastAction.loading("Création du projet en cours"));

        AlbumProjectRequester
            .createProject(new CreateAlbumProjectRequest(artist.id, albumArtBase64File, albumName))
            .then(response => {
                navigate(Paths.ADD_TRACKS_TO_PROJECT.resolve(response.projectId))
            })
    };


    return (
        <FadeInPage
            label={`Rajouter un album à l'artist ${artist.name}`}
            icon={<ConeStriped />}
            onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
        >
            <div className={CssTools.of().defaultPadding().css()}>

                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                    <Form.Label>Nom de l'album</Form.Label>
                    <Form.Control type="text" placeholder="Nom de l'album"
                        isValid={albumNameValid}
                        isInvalid={!albumNameValid}
                        onChange={(event) => setAlbumName(event.target.value)}
                        value={albumName}
                    />
                </Form.Group>

                <ImageUploadComponent title="Choix de l'album art" onImageAsBase64Loaded={(event) => setAlbumArtBase64File(event)} />
            </div>

            <MenuComponent>
                <MenuItemBackComponent
                    onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
                />
                <MenuItemApplyComponent
                    valid={albumNameValid && albumArtBase64File !== ""}
                    onClick={() => createProjectAndGoToAddTracks()}
                />
            </MenuComponent>
        </FadeInPage>
    );
}

export default CreateAlbumProjectComponent;