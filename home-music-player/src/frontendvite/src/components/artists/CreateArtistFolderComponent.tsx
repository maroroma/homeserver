import { useEffect, useState, type FC } from "react";
import Paths from "../../tools/routes/Paths";
import FadeInPage from "../FadeInPage";
import { useCustomNavigate, } from "../hooks/CustomHooks";
import { FolderPlus } from "react-bootstrap-icons";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { Form, Image, InputGroup } from "react-bootstrap";

import "./CreateArtistFolderComponent.css";
import ImageUploadComponent from "../uploaders/ImageUploadComponent";
import CssTools from "../../tools/CssTools";
import MenuItemApplyComponent from "../menu/MenuItemApplyComponent";
import { ToastAction } from "../../state/actions/ToastAction";
import { AddNewArtistFolderRequest, LibraryRequester } from "../../api/requesters/LibraryRequester";

const CreateArtistFolderComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { dispatch } = useMusicPlayerContext();


  const [artistName, setArtistName] = useState("");
  const [artistNameValid, setArtistNameValid] = useState(false);

  const [thumbAsBase64, setThumbAsBase64] = useState<string>("");
  const [fanartAsBase64, setFanartAsBase64] = useState<string>("");

  useEffect(() => {
    setArtistNameValid(artistName !== "")
  }, [artistName]);

  const createFolder = () => {
    dispatch(ToastAction.loading("Création du nouveau répetoire"));
    LibraryRequester.createArtistFolder(new AddNewArtistFolderRequest(artistName, thumbAsBase64, fanartAsBase64))
      .then(() => {
        dispatch(ToastAction.close());
        navigate(Paths.ALL_ARTISTS.resolve());
      })
  }


  return (
    <FadeInPage
      label="Créer un artiste"
      icon={<FolderPlus />}
      onClick={() => navigate(Paths.ADD_ARTIST.resolve())}

    >
      <div className={CssTools.of().defaultPadding().css()}>
        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
          <Form.Label>Nom de l'artiste</Form.Label>
          <Form.Control type="text" placeholder="Nom de l'artiste"
            isValid={artistNameValid}
            isInvalid={!artistNameValid}
            onChange={(event) => setArtistName(event.target.value)}
            value={artistName}
          />
        </Form.Group>

        <ImageUploadComponent title="Miniature pour l'artiste" onImageAsBase64Loaded={(image) => setThumbAsBase64(image)} />
        <ImageUploadComponent title="Fanart pour l'artiste" onImageAsBase64Loaded={(image) => setFanartAsBase64(image)} />

      </div>
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ADD_ARTIST.resolve())}
        />
        <MenuItemApplyComponent
          valid={artistNameValid && thumbAsBase64 !== "" && fanartAsBase64 !== ""}
          onClick={() => createFolder()}
        />
      </MenuComponent>
    </FadeInPage>
  );
};

export default CreateArtistFolderComponent;
