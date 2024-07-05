package maroroma.homemusicplayer.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.messaging.api.SimpleBroadcastNotification;
import maroroma.homemusicplayer.model.player.api.AbstractAlbumOrArtistSourceRequest;
import maroroma.homemusicplayer.model.player.api.AddAlbumToPlayListRequest;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.mp3.Mp3Player;
import maroroma.homemusicplayer.tools.CustomAssert;
import maroroma.homemusicplayer.tools.PlayList;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.*;

import static maroroma.homemusicplayer.tools.CustomAssert.trackIdNotNull;

/**
 * Parallélise la gestion de la playliste (comme objet fonctionnel) et du player, que l'on alimente en récupérant
 * les morceaux à jouer dans la playlist
 * Porte aussi le statut courant exposer au messagebroker via {@link PlayerMonitor}
 */
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final AlbumService albumService;

    private final ArtistService artistService;

    private final Mp3Player mp3Player;

    private final FilesFactory filesFactory;

    private final InputStreamCache inputStreamCache;

    private final ApplicationEventPublisher applicationEventPublisher;


    @Getter
    private PlayList playList = PlayList.empty();

    public PlayerStatus getPlayerStatus() {
        return this.mp3Player.getPlayerStatus();
    }

    public void play(CreatePlayerRequest createPlayerRequest) {
        Assert.notNull(createPlayerRequest, "createPlayerRequest can't be null");
        trackIdNotNull(createPlayerRequest.getTrackId());

        CustomAssert.notAllNotNull("artistId AND albumId can't be both given", createPlayerRequest.getAlbumId(), createPlayerRequest.getArtistId());
        CustomAssert.notAllNull("artistId AND albumId can't be both null", createPlayerRequest.getAlbumId(), createPlayerRequest.getArtistId());

        extractTracks(createPlayerRequest)
                .ifPresentOrElse(tracks -> {
                    this.playList = PlayList.of(createPlayerRequest.getTrackId(), tracks);
                    this.mp3Player.play(this.playList.getCurrentTrack(), endedTrack -> this.next());
                    this.inputStreamCache.populate(this.playList);
                }, () -> this.applicationEventPublisher.publishEvent(SimpleBroadcastNotification.error("Aucun morceau n'est accessible")));

    }


    /**
     * Ajoute un album a une playlist en cours
     *
     * @param addAlbumToPlayListRequest
     */
    public void addAlbumToPlayList(AddAlbumToPlayListRequest addAlbumToPlayListRequest) {
        Assert.notNull(addAlbumToPlayListRequest, "addAlbumToPlayListRequest can't be null");

        CustomAssert.notAllNotNull("album and artist can't be both requested", addAlbumToPlayListRequest.getAlbumId(), addAlbumToPlayListRequest.getArtistId());
        CustomAssert.notAllNull("album and artist can't be both null", addAlbumToPlayListRequest.getAlbumId(), addAlbumToPlayListRequest.getArtistId());

        extractTracks(addAlbumToPlayListRequest)
                .ifPresentOrElse(tracks -> {
                    var wasEmpty = this.playList.isEmpty();
                    this.playList = this.playList.addAll(tracks);

                    // si la liste était vide, on déclenche la lecture
                    if (wasEmpty) {

                        this.mp3Player.play(this.playList.getCurrentTrack(), endedTrack -> this.next());
                    }
                }, () -> this.applicationEventPublisher.publishEvent(SimpleBroadcastNotification.error("Aucun morceau n'est accessible")));



    }


    public void stop() {
        this.playList = PlayList.empty();
        this.mp3Player.stop();
    }

    public void pause() {
        this.mp3Player.pause();
    }

    public void resume() {
        this.mp3Player.resume();
    }

    public void next() {
        this.playList = this.playList.next();
        this.mp3Player.play(this.playList.getCurrentTrack(), endedTrack -> this.next());
        this.inputStreamCache.populate(this.playList);
    }

    public void previous() {
        this.playList = this.playList.previous();
        this.mp3Player.play(this.playList.getCurrentTrack(), endedTrack -> this.next());
    }

    public void volumeUp() {
        this.mp3Player.getVolumeControl().volumeUp();
    }

    public int getVolume() {
        return this.mp3Player.getVolumeControl().getCurrentVolume();
    }

    public void volumeDown() {
        this.mp3Player.getVolumeControl().volumeDown();
    }

    /**
     * Récup des morceaux à jouer
     * réalise un filtrage des fichiers non accessibles, et peut donc retourner un optional vide si liste a été intégralement purgée
     * @param abstractAlbumOrArtistSourceRequest
     * @return
     */
    private Optional<List<TrackEntity>> extractTracks(AbstractAlbumOrArtistSourceRequest abstractAlbumOrArtistSourceRequest) {
        CustomAssert.notAllNotNull("album and artist can't be both requested", abstractAlbumOrArtistSourceRequest.getAlbumId(), abstractAlbumOrArtistSourceRequest.getArtistId());
        CustomAssert.notAllNull("album and artist can't be both null", abstractAlbumOrArtistSourceRequest.getAlbumId(), abstractAlbumOrArtistSourceRequest.getArtistId());

        List<TrackEntity> tracks = new ArrayList<>();

        abstractAlbumOrArtistSourceRequest.withAlbumId()
                .flatMap(albumService::getAlbum)
                .map(AlbumEntity::getTracks)
                .ifPresent(tracks::addAll);

        abstractAlbumOrArtistSourceRequest.withArtistId()
                .map(artistService::getTracksFromArtist)
                .ifPresent(tracks::addAll);

        return Optional.of(tracks.stream()
                        .filter(trackEntity -> this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath()).exists())
                        .toList())
                .filter(Predicate.not(Collection::isEmpty));
    }
}
