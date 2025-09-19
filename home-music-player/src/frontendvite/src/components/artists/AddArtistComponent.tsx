import {type FC, useState} from "react";
import FadeInPage from "../FadeInPage";
import {PersonAdd, Plus} from "react-bootstrap-icons";
import Paths from "../../tools/routes/Paths";
import {useCustomNavigate, useLoadingEffect} from "../hooks/CustomHooks";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import type {SimpleFile} from "../../api/model/files/SimpleFile";
import {Comparators} from "../../tools/Comparators";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ToastAction} from "../../state/actions/ToastAction";

const AddArtistComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { dispatch } = useMusicPlayerContext();

  const [allArtists, setAllArtists] = useState<SimpleFile[]>([]);

  useLoadingEffect("Nouveaux Artistes en cours de chargement", () =>
    LibraryRequester.getArtistCandidates().then((result) =>
      setAllArtists(
        result.sort(Comparators.by((simpleFile) => simpleFile.name))
      )
    )
  );

  const scanArtist = (selectedDirectory: SimpleFile) => {
    dispatch(
      ToastAction.loading(
        `Scan de l'artiste ${selectedDirectory.name} en cours`
      )
    );

    LibraryRequester.createArtist(selectedDirectory).then(() => {
      dispatch(ToastAction.close());
      navigate(Paths.ALL_ARTISTS.resolve());
    });
  };

  return (
    <FadeInPage
      label="Ajouter un artiste"
      icon={<PersonAdd />}
      onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
    >
      {allArtists.map((anArtistCandidate) => (
        <IconListItemRenderer
          icon={<Plus />}
          label={anArtistCandidate.name}
          size="xsmall"
          onClick={() => scanArtist(anArtistCandidate)}
        />
      ))}
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
        />
      </MenuComponent>
    </FadeInPage>
  );
};

export default AddArtistComponent;
