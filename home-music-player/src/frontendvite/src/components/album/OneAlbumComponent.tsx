import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useAlbum, useArtist, useCustomNavigate, useLoadingEffect, } from "../hooks/CustomHooks";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import { NameTransformer } from "../../tools/NameTransformer";
import FanArtComponent from "../fanart/FanArtComponent";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import MenuItemAddToPlayListComponent from "../menu/MenuItemAddToPlayListComponent";
import { Comparators } from "../../tools/Comparators";
import MenuItemRemoveAlbumComponent from "../menu/MenuItemRemoveAlbumComponent";
import YesNoModal from "../modals/YesNoModal";
import { ToastAction } from "../../state/actions/ToastAction";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import MenuItemAddComponent from "../menu/MenuItemAddComponent";
import { AlbumProjectRequester } from "../../api/requesters/AlbumProjectRequester";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";
import { LoadNewTrackListAction } from "../../state/actions/LoadNewTrackListAction";
import { AddTracksToTrackListAction } from "../../state/actions/AddTracksToTrackListAction";
import MenuItemSubMenu from "../menu/MenuItemSubMenu";
import MenuItemSwitchPlayListComponent from "../menu/MenuItemSwitchPlayListComponent";
import MenuItemSwitchDownloadComponent from "../menu/MenuItemSwitchDownloadComponent";
import TrackListItemRenderer, { type TrackDisplayMode } from "../renderers/TrackListItemRenderer";

const OneAlbumComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { album } = useAlbum();
  const { artist, artistId } = useArtist();
  const { dispatch } = useMusicPlayerContext();

  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const [allTracks, setAllTracks] = useState<Track[]>([]);

  const [trackDisplayMode, setTrackDisplayMode] = useState<TrackDisplayMode>("Play");

  useLoadingEffect(
    "Morceaux en cours de chargement",
    async () => {
      if (album && album.id) {
        const results = await LibraryRequester.getTracksForAlbum(album);
        return setAllTracks(results.sort(Comparators.byTrackName()));
      }
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

    EmbeddedPlayerRequester.generareTrackListFromAlbum(album)
      .then(trackList => {
        dispatch(new AddTracksToTrackListAction(trackList))
      })

  };

  const createAlbumUpdateProject = () => {
    dispatch(ToastAction.loading("Préparation de l'ajout"));
    AlbumProjectRequester.createProjectFromExistingAlbum(album)
      .then(response => navigate(Paths.ADD_TRACKS_TO_PROJECT.resolve(response.projectId)))
  }


  return (
    <FadeInPage
      label={NameTransformer.albumName(album, artist)}
      libraryItemArts={album.libraryItemArts}
      onClick={() => navigate(Paths.ONE_ARTIST.resolve(artistId))}
    >
      {allTracks.map((aTrack) => (
        <TrackListItemRenderer
          size="xsmall"
          track={aTrack}
          key={aTrack.id}
          displayMode={trackDisplayMode}
          onPlay={() =>
            EmbeddedPlayerRequester.generareTrackListFromAlbum(album)
              .then(response => dispatch(new LoadNewTrackListAction(response, aTrack.id)))}
          onAddToFavorite={() =>
            navigate(Paths.ADD_TO_PLAYLIST_FROM_ALBUM.resolve([aTrack.id, artist.id, album.id]))
          }
        />
      ))}
      <FanArtComponent fanart={artist.libraryItemArts} />
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ONE_ARTIST.resolve(artistId))}
        />

        <MenuItemAddToPlayListComponent onClick={() => addAlbumToPlayList()} />

        <MenuItemSubMenu>

          <MenuItemSwitchPlayListComponent
            on={trackDisplayMode === "Favorite"}
            onClick={() => {
              trackDisplayMode === "Favorite" ? setTrackDisplayMode("Play") : setTrackDisplayMode("Favorite");
            }}
          />

          <MenuItemSwitchDownloadComponent
            on={trackDisplayMode === "Download"}
            onClick={() => {
              trackDisplayMode === "Download" ? setTrackDisplayMode("Play") : setTrackDisplayMode("Download");
            }}
          />

          <MenuItemAddComponent
            onClick={() => createAlbumUpdateProject()}
          />
          <MenuItemRemoveAlbumComponent
            onClick={() => setDisplayDeletePopup(true)}
          />
        </MenuItemSubMenu>


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
