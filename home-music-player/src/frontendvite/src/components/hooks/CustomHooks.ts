import {type DependencyList, useEffect, useState} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ToastAction} from "../../state/actions/ToastAction";
import {type To, useNavigate, useParams} from "react-router";
import {Artist} from "../../api/model/library/Artist";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import {Album} from "../../api/model/library/Album";
import WindowTool from "../../tools/WindowTool";

const useLoadingEffect = <T>(message: string, promiseSupplier: () => Promise<T>, deps: DependencyList = []) => {
  const { dispatch } = useMusicPlayerContext();

  useEffect(() => {
    dispatch(ToastAction.loading(message));
    promiseSupplier()
      .then(() => dispatch(ToastAction.close()))
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);
};

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


export { useLoadingEffect, useArtist, useAlbum, useCustomNavigate };
