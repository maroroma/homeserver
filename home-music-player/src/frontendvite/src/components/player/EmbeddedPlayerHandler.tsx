import { useEffect, useRef, type FC } from "react";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { LibraryRequester } from "../../api/requesters/LibraryRequester";
import { UpdateAlbumOnPlayerStatusAction } from "../../state/actions/UpdateAlbumOnPlayerStatusAction";
import { UpdateArtistOnPlayerStatusAction } from "../../state/actions/UpdateArtistOnPlayerStatusAction";
import { NextTrackAction } from "../../state/actions/NextTrackAction";
import { ToastAction } from "../../state/actions/ToastAction";
import { EmbeddedPlayerRequester } from "../../api/requesters/EmbeddedPlayerRequester";

const EmbeddedPlayerHandler: FC = () => {

    const { embeddedPlayerState, dispatch, playerStatus } = useMusicPlayerContext();

    const audioRef = useRef<HTMLAudioElement>(null);

    useEffect(() => {
        if (playerStatus.track && playerStatus.track.albumId) {
            LibraryRequester.getAlbum(playerStatus.track.albumId)
                .then(album => {
                    dispatch(new UpdateAlbumOnPlayerStatusAction(album));
                    return LibraryRequester.getArtist(album.artistId)
                })
                .then(artist => {
                    dispatch(new UpdateArtistOnPlayerStatusAction(artist))
                })
        }
    }, [playerStatus.track]);

    useEffect(() => {
        if (audioRef.current) {
            audioRef.current.volume = playerStatus.volume / 100;
        }
    }, [playerStatus.volume]);

    useEffect(() => {
        if (audioRef.current) {
            if (playerStatus.playerStatus === "PAUSED") {
                audioRef.current.pause();
            }

            if (playerStatus.playerStatus === "PLAYING") {
                audioRef.current.play();
            }
        }
    }, [playerStatus.playerStatus])

    return <h1>
        {embeddedPlayerState.trackList.tracks.length > 0 && embeddedPlayerState.currentIndex !== -1 ?
            <audio
                ref={audioRef}
                controls={false}
                autoPlay
                onLoadStart={() => dispatch(ToastAction.loadingTrack())}
                onLoadedData={() => dispatch(ToastAction.close())}
                onEnded={() => dispatch(new NextTrackAction())}
                // en cas d'erreur on tente de passer à un autre morceau, sinon le lecteur reste bloqué en mode chargement
                onError={() => dispatch(new NextTrackAction())}
                // onTimeUpdate={(value) => console.log("timeupdate", value.timeStamp, value.currentTarget.currentTime)}
                src={EmbeddedPlayerRequester.trackDownloadUrl(playerStatus.track)}
            ></audio>
            : <></>
        }
    </h1>
}

export default EmbeddedPlayerHandler;