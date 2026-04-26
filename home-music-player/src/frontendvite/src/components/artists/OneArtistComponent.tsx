import {type FC, useState} from "react";
import Paths from "../../tools/routes/Paths";
import FadeInPage from "../FadeInPage";
import {useArtist, useCustomNavigate, useLoadingEffect,} from "../hooks/CustomHooks";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import {Album} from "../../api/model/library/Album";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import {List, Plus} from "react-bootstrap-icons";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import FanArtComponent from "../fanart/FanArtComponent";
import MenuItemRemoveArtistComponent from "../menu/MenuItemRemoveArtistComponent";
import YesNoModal from "../modals/YesNoModal";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ToastAction} from "../../state/actions/ToastAction";
import {Comparators} from "../../tools/Comparators";
import { NameTransformer } from "../../tools/NameTransformer";

const OneArtistComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { dispatch } = useMusicPlayerContext();
  const [albums, setAlbums] = useState<Album[]>([]);
  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const { artist } = useArtist();

  useLoadingEffect(
    "Albums de l'artiste en cours de chargement",
    async () => {
      const results = await LibraryRequester.getAlbumsForArtist(artist);
      return setAlbums(results.sort(Comparators.by((album) => album.name)));
    },
    [artist]
  );

  const deleteArtist = () => {
    dispatch(ToastAction.loading("Suppression de l'artiste en cours"));
    LibraryRequester.deleteArtist(artist)
      .then(() => dispatch(ToastAction.close()))
      .then(() => navigate(Paths.ALL_ARTISTS.resolve()));
  };

  return (
    <FadeInPage
      label={artist.name}
      libraryItemArts={artist.libraryItemArts}
      onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
    >
      <IconListItemRenderer
        icon={<List />}
        label="Toutes les musiques"
        onClick={() => navigate(Paths.ALL_TRACKS_FOR_ARTIST.resolve(artist.id))}
      />
      {albums.map((anAlbum) => (
        <LibraryListItemRenderer
          label={NameTransformer.albumName(anAlbum, artist)}
          libraryItemArts={anAlbum.libraryItemArts}
          key={anAlbum.id}
          onClick={() =>
            navigate(Paths.ONE_ALBUM.resolve([artist.id, anAlbum.id]))
          }
        />
      ))}
      <IconListItemRenderer
        icon={<Plus />}
        label="Ajouter un album"
        onClick={() => {
          const test = Paths.ADD_ALBUM.resolve(artist.id);
          console.log("test", test)
          navigate(Paths.ADD_ALBUM.resolve(artist.id))}
        }
      />
      <FanArtComponent fanart={artist.libraryItemArts} />
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ALL_ARTISTS.resolve())}
        />
        <MenuItemRemoveArtistComponent
          onClick={() => setDisplayDeletePopup(true)}
        />
      </MenuComponent>
      <YesNoModal
        message={
          <>
            Voulez-vous supprimer l'artiste <strong>{artist.name}</strong> ?
          </>
        }
        title="Suppression d'un artiste"
        show={displayDeletePopup}
        onNo={() => setDisplayDeletePopup(false)}
        onYes={() => {
          setDisplayDeletePopup(false);
          deleteArtist();
        }}
        yesColor="danger"
        noColor="dark"
      ></YesNoModal>
    </FadeInPage>
  );
};

export default OneArtistComponent;
