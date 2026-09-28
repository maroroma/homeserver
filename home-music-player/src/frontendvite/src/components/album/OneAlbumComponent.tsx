import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useAlbum, useArtist, useCustomNavigate, useLoadingEffect, } from "../hooks/CustomHooks";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import { NameTransformer } from "../../tools/NameTransformer";
import { BookmarkPlus, Play } from "react-bootstrap-icons";
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

const OneAlbumComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { album } = useAlbum();
  const { artist, artistId } = useArtist();
  const { dispatch } = useMusicPlayerContext();

  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const [allTracks, setAllTracks] = useState<Track[]>([]);

  const [playListDisplayMode, setPlayListDisplayMode] = useState(false);
  const [downloadDisplayMode, setDownloadDisplayMode] = useState(false);

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
        <IconListItemRenderer
          size="xsmall"
          icon={playListDisplayMode ? <BookmarkPlus /> : <Play />}
          enableDownload={downloadDisplayMode}
          downloadTitle={aTrack.shortFileName}
          downloadLink={EmbeddedPlayerRequester.trackDownloadUrl(aTrack)}
          label={NameTransformer.trackName(aTrack)}
          key={aTrack.id}
          onClick={() => {
            if (playListDisplayMode) {
              navigate(Paths.ADD_TO_PLAYLIST_FROM_ALBUM.resolve([aTrack.id, artist.id, album.id]))
            } else {
              EmbeddedPlayerRequester.generareTrackListFromAlbum(album)
                .then(response => dispatch(new LoadNewTrackListAction(response, aTrack.id)));
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

        <MenuItemSubMenu>

          <MenuItemSwitchPlayListComponent
            on={playListDisplayMode}
            onClick={() => {
              setPlayListDisplayMode(!playListDisplayMode)
              setDownloadDisplayMode(false)
            }}
          />

          <MenuItemSwitchDownloadComponent
            on={downloadDisplayMode}
            onClick={() => {
              setDownloadDisplayMode(!downloadDisplayMode)
              setPlayListDisplayMode(false)
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
