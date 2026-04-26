import {type FC, useState} from "react";
import FadeInPage from "../FadeInPage";
import {Plus} from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import {useArtist, useCustomNavigate, useLoadingEffect} from "../hooks/CustomHooks";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import type {SimpleFile} from "../../api/model/files/SimpleFile";
import {Comparators} from "../../tools/Comparators";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ToastAction} from "../../state/actions/ToastAction";
import {Alert} from "react-bootstrap";
import MenuItemCreateAlbumComponent from "../menu/MenuItemCreateAlbumComponent";

const AddAlbumComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { dispatch } = useMusicPlayerContext();


  const {artist} = useArtist();

  const [allAlbums, setAllAlbums] = useState<SimpleFile[]>([]);

  useLoadingEffect("Nouveaux Albums en cours de chargement", () =>
    LibraryRequester.getAlbumCandidates(artist).then((result) =>
      setAllAlbums(
        result.sort(Comparators.by((simpleFile) => simpleFile.name))
      )
    ),
    [artist]
  );

  const scanAlbum = (selectedDirectory: SimpleFile) => {
    dispatch(
      ToastAction.loading(
        `Scan de l'album ${selectedDirectory.name} en cours`
      )
    );

    LibraryRequester.addAlbumToArtist(selectedDirectory, artist).then(() => {
      dispatch(ToastAction.close());
      navigate(Paths.ONE_ARTIST.resolve(artist.id));
    });
  };

  return (
    <FadeInPage
      label={`Ajout un Album à l'artiste ${artist.name}`}
      icon={<Plus />}
      onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
    >
      {allAlbums.map((anAlbumCandidate) => (
        <IconListItemRenderer
          icon={<Plus />}
          label={anAlbumCandidate.name}
          size="xsmall"
          onClick={() => scanAlbum(anAlbumCandidate)}
        />
      ))}
      {allAlbums.length === 0 ? <Alert className="clickable" variant="warning" onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}>
          Pas de nouvel album à scanner....
        </Alert> : <></>}
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ONE_ARTIST.resolve(artist.id))}
        />
        <MenuItemCreateAlbumComponent />
      </MenuComponent>
    </FadeInPage>
  );
};

export default AddAlbumComponent;
