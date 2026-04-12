import { type DependencyList, useEffect, useState } from "react";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { ToastAction } from "../../state/actions/ToastAction";
import { type To, useNavigate, useParams } from "react-router";
import { Artist } from "../../api/model/library/Artist";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import { Album } from "../../api/model/library/Album";
import WindowTool from "../../tools/WindowTool";
import { PlayList } from "../../api/model/playlists/Playlist";
import { PlayListRequester } from "../../api/requesters/PlayListRequester";

const useLoadingEffect = <T>(message: string, promiseSupplier: () => Promise<T>, deps: DependencyList = []) => {
  const { dispatch } = useMusicPlayerContext();

  useEffect(() => {
    dispatch(ToastAction.loading(message));
    promiseSupplier()
      .then(() => dispatch(ToastAction.close()))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);
};

const useAllPlayLists = () => {
  const [allPlayLists, setAllPlayLists] = useState<PlayList[]>([]);
  const { dispatch } = useMusicPlayerContext();

  useEffect(() => {
    dispatch(ToastAction.loading("Chargement des playlists"))

    PlayListRequester.getAllPlayList()
      .then(response => setAllPlayLists(response))
      .then(() => dispatch(ToastAction.close()))
  }, [])

  return { allPlayLists, setAllPlayLists }
}

const usePlayList = () => {
  const { playListId } = useParams();
  const { dispatch } = useMusicPlayerContext();
  const [playList, setPlayList] = useState(PlayList.empty());

  useEffect(() => {
    if (playListId) {
      dispatch(ToastAction.loading("Chargement de la playList en cours"))
      PlayListRequester.getOnePlayList(playListId)
        .then(result => setPlayList(result))
        .then(() => dispatch(ToastAction.close()))
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [playListId]);

  return { playListId, playList, setPlayList }
}

const useArtist = () => {
  const { artistId } = useParams();
  const { dispatch } = useMusicPlayerContext();
  const [artist, setArtist] = useState(Artist.empty());

  useEffect(() => {
    if (artistId) {
      dispatch(ToastAction.loading("Chargement de l'artiste en cours"))
      LibraryRequester.getArtist(artistId)
        .then(result => setArtist(result))
        .then(() => dispatch(ToastAction.close()))
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [artistId]);

  return { artistId, artist, setArtist }
}

const useAlbum = () => {
  const { albumId } = useParams();
  const { dispatch } = useMusicPlayerContext();
  const [album, setAlbum] = useState(Album.empty());

  useEffect(() => {
    if (albumId) {
      dispatch(ToastAction.loading("Chargement de l'album en cours"))
      LibraryRequester.getAlbum(albumId)
        .then(result => setAlbum(result))
        .then(() => dispatch(ToastAction.close()))
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [albumId]);

  return { albumId, album, setAlbum }
}

const useCustomNavigate: (() => ((to: To) => void)) = () => {
  const nativeNavigate = useNavigate();

  return (to: To) => {
    WindowTool.scrollToTop();
    return nativeNavigate(to);
  }

}


export { useLoadingEffect, useArtist, useAlbum, useCustomNavigate, useAllPlayLists, usePlayList };
