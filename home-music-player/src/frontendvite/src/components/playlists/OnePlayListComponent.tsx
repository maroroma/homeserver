import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useCustomNavigate, useLoadingEffect, usePlayList, } from "../hooks/CustomHooks";
import { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import { NameTransformer } from "../../tools/NameTransformer";
import { BookmarkCheck, BookmarkStar, BookmarkX } from "react-bootstrap-icons";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import { Comparators } from "../../tools/Comparators";
import MenuItemRemoveAlbumComponent from "../menu/MenuItemRemoveAlbumComponent";
import YesNoModal from "../modals/YesNoModal";
import { ToastAction } from "../../state/actions/ToastAction";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { PlayListRequester } from "../../api/requesters/PlayListRequester";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import MenuItemAddToPlayListComponent from "../menu/MenuItemAddToPlayListComponent";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";
import { LoadNewTrackListAction } from "../../state/actions/LoadNewTrackListAction";
import { AddTracksToTrackListAction } from "../../state/actions/AddTracksToTrackListAction";
import MenuItemSubMenu from "../menu/MenuItemSubMenu";
import MenuItemSwitchDownloadComponent from "../menu/MenuItemSwitchDownloadComponent";
import MenuItemSwitchComponent from "../menu/MenuItemSwitchComponent";

const OnePlayListComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { playList, setPlayList } = usePlayList();
  const { dispatch } = useMusicPlayerContext();

  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const [displayRemovePopup, setDisplayRemovePopup] = useState(false);
  const [selectedTrackToRemove, setSelectedTrackToRemove] = useState(Track.empty());

  const [displayRemoveButtons, setDisplayRemoveButtons] = useState(false);
  const [displayDownloadButtons, setDisplayDownloadButtons] = useState(false);

  const [allTracks, setAllTracks] = useState<Track[]>([]);

  useLoadingEffect(
    "Morceaux en cours de chargement",
    async () => {
      if (playList && playList.playListId) {
        const results = await PlayListRequester.getTracksForPlayList(playList);
        return setAllTracks(results.sort(Comparators.byTrackName()));
      }
    },
    [playList]
  );

  const deletePlayList = () => {
    dispatch(ToastAction.loading("Suppression de la playList en cours"));
    PlayListRequester.deletePlayList(playList)
      .then(() => dispatch(ToastAction.close()))
      .then(() => navigate(Paths.ALL_PLAYLISTS.resolve()));
  };

  const removeTrackFromPlayList = () => {
    dispatch(ToastAction.loading("mise à jour de la playlist en cours"));
    PlayListRequester.removeTrackFromPlayList(playList, selectedTrackToRemove.id)
      .then(response => {
        setPlayList(response);
        dispatch(ToastAction.close())
      })
      .then(() => setSelectedTrackToRemove(Track.empty()))
  }

  const addPlayListToPlayList = () => {
    EmbeddedPlayerRequester.generareTrackListFromPlayList(playList)
      .then(response => dispatch(new AddTracksToTrackListAction(response)));
  };

  return (
    <FadeInPage
      label={playList.name}
      icon={<BookmarkStar />}
      onClick={() => navigate(Paths.ALL_PLAYLISTS.resolve())}
    >
      {allTracks.map((aTrack) => (

        displayRemoveButtons ?
          <IconListItemRenderer
            icon={<BookmarkX />}
            size="medium"
            label={NameTransformer.trackName(aTrack)}
            onClick={() => {
              setDisplayRemovePopup(true);
              setSelectedTrackToRemove(aTrack)
            }}

          /> : <LibraryListItemRenderer
            label={NameTransformer.trackName(aTrack)}
            key={aTrack.id}
            size="medium"
            libraryItemArts={new LibraryItemArts(null, null, aTrack.albumId)}
            enableDownload={displayDownloadButtons}
            downloadLink={EmbeddedPlayerRequester.trackDownloadUrl(aTrack)}
            downloadTitle={aTrack.shortFileName}
            onClick={() => {
              EmbeddedPlayerRequester.generareTrackListFromPlayList(playList)
                .then(response => dispatch(new LoadNewTrackListAction(response, aTrack.id)))
            }}
          />


      ))}
      {/* <FanArtComponent fanart={artist.libraryItemArts} /> */}
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ALL_PLAYLISTS.resolve())}
        />

        <MenuItemAddToPlayListComponent onClick={() => addPlayListToPlayList()} />


        <MenuItemSubMenu>

          <MenuItemSwitchDownloadComponent
            on={displayDownloadButtons}
            onClick={() => {
              setDisplayDownloadButtons(!displayDownloadButtons)
              setDisplayRemoveButtons(false)
            }}
          />

          <MenuItemSwitchComponent
            on={displayRemoveButtons}
            onElement={<BookmarkX />}
            offElement={<BookmarkCheck />}
            onClick={() => {
              setDisplayRemoveButtons(!displayRemoveButtons)
              setDisplayDownloadButtons(false);
            }}
          />

          <MenuItemRemoveAlbumComponent
            onClick={() => setDisplayDeletePopup(true)}
          />

        </MenuItemSubMenu>
      </MenuComponent>


      <YesNoModal
        message={
          <>
            Voulez-vous supprimer la morceau <strong>{selectedTrackToRemove.name}</strong> de la playList <strong>{playList.name}</strong> ?
          </>
        }
        title="Suppression d'un morceau de la playlist"
        show={displayRemovePopup}
        onNo={() => setDisplayRemovePopup(false)}
        onYes={() => {
          setDisplayRemovePopup(false);
          removeTrackFromPlayList();
        }}
        yesColor="danger"
        noColor="dark"
      ></YesNoModal>


      {/* suppression complete de la playlist */}
      <YesNoModal
        message={
          <>
            Voulez-vous supprimer la playList <strong>{playList.name}</strong> ?
          </>
        }
        title="Suppression d'une playlist"
        show={displayDeletePopup}
        onNo={() => setDisplayDeletePopup(false)}
        onYes={() => {
          setDisplayDeletePopup(false);
          deletePlayList();
        }}
        yesColor="danger"
        noColor="dark"
      ></YesNoModal>


    </FadeInPage>
  );
};

export default OnePlayListComponent;
