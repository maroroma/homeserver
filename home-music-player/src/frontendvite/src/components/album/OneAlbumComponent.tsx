import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useAlbum, useArtist, useCustomNavigate, useLoadingEffect, } from "../hooks/CustomHooks";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import { NameTransformer } from "../../tools/NameTransformer";
import { BookmarkCheck, BookmarkPlus, BookmarkX, Play } from "react-bootstrap-icons";
import FanArtComponent from "../fanart/FanArtComponent";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import MenuItemAddToPlayListComponent from "../menu/MenuItemAddToPlayListComponent";
import { Comparators } from "../../tools/Comparators";
import MenuItemRemoveAlbumComponent from "../menu/MenuItemRemoveAlbumComponent";
import YesNoModal from "../modals/YesNoModal";
import { ToastAction } from "../../state/actions/ToastAction";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { PlayerRequester } from "../../api/requesters/PlayerRequester";
import MenuItemComponent from "../menu/MenuItemComponent";

const OneAlbumComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { album } = useAlbum();
  const { artist, artistId } = useArtist();
  const { dispatch } = useMusicPlayerContext();

  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const [allTracks, setAllTracks] = useState<Track[]>([]);

  const [playListDisplayMode, setPlayListDisplayMode] = useState(false);

  useLoadingEffect(
    "Morceaux en cours de chargement",
    async () => {
      const results = await LibraryRequester.getTracksForAlbum(album);
      return setAllTracks(results.sort(Comparators.byTrackName()));
    },
    [album]
  );

  const deleteAlbum = () => {
    dispatch(ToastAction.loading("Suppression de l'album en cours"));
    LibraryRequester.deleteAlbum(artist, album)
      .then(() => dispatch(ToastAction.close()))
      .then(() => navigate(Paths.ONE_ARTIST.resolve(artist.id)));
  };

  const addAlbumToPlayList = () => {
    dispatch(ToastAction.loading("Album en cours d'ajout à la playlist"));
    PlayerRequester.addAlbumToPlayList(album).then(() =>
      dispatch(ToastAction.close())
    );
  };

  return (
    <FadeInPage
      label={album.name}
      libraryItemArts={album.libraryItemArts}
      onClick={() => navigate(Paths.ONE_ARTIST.resolve(artistId))}
    >
      {allTracks.map((aTrack) => (
        <IconListItemRenderer
          size="xsmall"
          icon={playListDisplayMode ? <BookmarkPlus /> : <Play />}
          label={NameTransformer.trackName(aTrack)}
          key={aTrack.id}
          onClick={() => {
            if (playListDisplayMode) {
              navigate(Paths.ADD_TO_PLAYLIST_FROM_ALBUM.resolve([aTrack.id, artist.id, album.id]))
            } else {
              dispatch(ToastAction.loadingTrack());
              PlayerRequester.startPlayer(album, aTrack).then(() => {
                navigate(Paths.PLAYER.resolve());
                dispatch(ToastAction.close());
              });
            }
          }}
        />
      ))}
      <FanArtComponent fanart={artist.libraryItemArts} />
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ONE_ARTIST.resolve(artistId))}
        />

        <MenuItemAddToPlayListComponent onClick={() => addAlbumToPlayList()} />
        <MenuItemComponent icon={playListDisplayMode ? <BookmarkX size={40} /> : <BookmarkCheck size={40} />} onClick={() => {
          setPlayListDisplayMode(!playListDisplayMode)
        }} />

        <MenuItemRemoveAlbumComponent
          onClick={() => setDisplayDeletePopup(true)}
        />
      </MenuComponent>
      <YesNoModal
        message={
          <>
            Voulez-vous supprimer l'album <strong>{album.name}</strong> ?
          </>
        }
        title="Suppression d'un album"
        show={displayDeletePopup}
        onNo={() => setDisplayDeletePopup(false)}
        onYes={() => {
          setDisplayDeletePopup(false);
          deleteAlbum();
        }}
        yesColor="danger"
        noColor="dark"
      ></YesNoModal>
    </FadeInPage>
  );
};

export default OneAlbumComponent;
