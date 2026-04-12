import { type FC, useState } from "react";
import Paths from "../../tools/routes/Paths";
import { useCustomNavigate, useLoadingEffect, usePlayList, } from "../hooks/CustomHooks";
import { Track } from "../../api/model/library/Track";
import FadeInPage from "../FadeInPage";
import { NameTransformer } from "../../tools/NameTransformer";
import { BookmarkStar, Trash } from "react-bootstrap-icons";
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

const OnePlayListComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { playList, setPlayList } = usePlayList();
  const { dispatch } = useMusicPlayerContext();

  const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

  const [displayRemovePopup, setDisplayRemovePopup] = useState(false);
  const [selectedTrackToRemove, setSelectedTrackToRemove] = useState(Track.empty());

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

  const addCustomPlayListToCurrentPlayList = () => {
    // dispatch(ToastAction.loading("Album en cours d'ajout à la playlist"));
    // PlayerRequester.addAlbumToPlayList(album).then(() =>
    //   dispatch(ToastAction.close())
    // );
  };

  return (
    <FadeInPage
      label={playList.name}
      icon={<BookmarkStar />}
      onClick={() => navigate(Paths.ALL_PLAYLISTS.resolve())}
    >
      {allTracks.map((aTrack) => (
        <Stack direction="horizontal">
          <LibraryListItemRenderer
            // label={aTrack.albumId}
            label={NameTransformer.trackName(aTrack)}
            key={aTrack.id}
            libraryItemArts={aTrack.libraryItemArts}
          />
          <Button variant="danger" onClick={() => {
            setSelectedTrackToRemove(aTrack);
            setDisplayRemovePopup(true);
          }}><Trash /></Button>
        </Stack>
      ))}
      {/* <FanArtComponent fanart={artist.libraryItemArts} /> */}
      <MenuComponent>
        <MenuItemBackComponent
          onClick={() => navigate(Paths.ALL_PLAYLISTS.resolve())}
        />

        <MenuItemAddToPlayListComponent />

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
