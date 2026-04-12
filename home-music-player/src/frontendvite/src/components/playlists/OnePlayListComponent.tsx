import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useCustomNavigate, useLoadingEffect, usePlayList, } from "../hooks/CustomHooks";
import { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import { NameTransformer } from "../../tools/NameTransformer";
import { BookmarkCheck, BookmarkStar, BookmarkX, Trash } from "react-bootstrap-icons";
import MenuComponent from "../menu/MenuComponent";
import MenuItemBackComponent from "../menu/MenuItemBackComponent";
import MenuItemAddToPlayListComponent from "../menu/MenuItemAddToPlayListComponent";
import { Comparators } from "../../tools/Comparators";
import MenuItemRemoveAlbumComponent from "../menu/MenuItemRemoveAlbumComponent";
import YesNoModal from "../modals/YesNoModal";
import { ToastAction } from "../../state/actions/ToastAction";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { PlayListRequester } from "../../api/requesters/PlayListRequester";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import { Button, Stack } from "react-bootstrap";
import { LibraryItemArts } from "../../api/model/library/LibraryItemArts";
import { PlayerRequester } from "../../api/requesters/PlayerRequester";
import MenuItemComponent from "../menu/MenuItemComponent";
import IconListItemRenderer from "../renderers/IconListItemRenderer";

const OnePlayListComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { playList, setPlayList } = usePlayList();
  const { dispatch } = useMusicPlayerContext();

  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const [displayRemovePopup, setDisplayRemovePopup] = useState(false);
  const [selectedTrackToRemove, setSelectedTrackToRemove] = useState(Track.empty());

  const [displayRemoveButtons, setDisplayRemoveButtons] = useState(false);

  const [allTracks, setAllTracks] = useState<Track[]>([]);

  useLoadingEffect(
    "Morceaux en cours de chargement",
    async () => {
      const results = await PlayListRequester.getTracksForPlayList(playList);
      return setAllTracks(results.sort(Comparators.byTrackName()));
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
            onClick={() => {
              dispatch(ToastAction.loadingTrack());
              PlayerRequester.startPlayerForPlayList(playList, aTrack)
                .then(() => {
                  navigate(Paths.PLAYER.resolve());
                  dispatch(ToastAction.close());
                })
            }}
          />


      ))}
      {/* <FanArtComponent fanart={artist.libraryItemArts} /> */}
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ALL_PLAYLISTS.resolve())}
        />

        <MenuItemComponent icon={displayRemoveButtons ? <BookmarkCheck size={40} /> : <BookmarkX size={40} />} onClick={() => {
          setDisplayRemoveButtons(!displayRemoveButtons)
        }} />

        {/* <MenuItemAddToPlayListComponent /> */}

        <MenuItemRemoveAlbumComponent
          onClick={() => setDisplayDeletePopup(true)}
        />
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
