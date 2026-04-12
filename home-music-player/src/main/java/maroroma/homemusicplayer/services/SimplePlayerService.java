package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.player.api.AbstractAlbumOrArtistSourceRequest;
import maroroma.homemusicplayer.model.player.api.AddAlbumToPlayListRequest;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;
import maroroma.homemusicplayer.services.mp3.Mp3SimplePlayerTask;
import maroroma.homemusicplayer.tools.CustomAssert;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.*;

import static maroroma.homemusicplayer.tools.CustomAssert.trackIdNotNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimplePlayerService implements PlayerService {

    private final AlbumService albumService;
    private final ArtistService artistService;
    private final PlayListService playListService;
    private final FilesFactory filesFactory;
    private final Mp3SimplePlayerTask mp3SimplePlayerTask;
    private final SynchronizedPlayList synchronizedPlayList;

    @Override
    public void play(CreatePlayerRequest createPlayerRequest) {
        log.info("PLAYER -> PLAY");

        Assert.notNull(createPlayerRequest, "createPlayerRequest can't be null");
        trackIdNotNull(createPlayerRequest.getTrackId());

        CustomAssert.notAllNotNull("artistId AND albumId AND playListId can't be all given",
                createPlayerRequest.getAlbumId(),
                createPlayerRequest.getArtistId(),
                createPlayerRequest.getPlayListId()
                );
        CustomAssert.notAllNull("artistId AND albumId AND playListId can't be all null",
                createPlayerRequest.getAlbumId(),
                createPlayerRequest.getArtistId(),
                createPlayerRequest.getPlayListId()
                );

        extractTracks(createPlayerRequest)
                .ifPresent(tracksToPlay -> this.synchronizedPlayList.clearAndAdd(createPlayerRequest.getTrackId(), tracksToPlay));

    }

    @Override
    public void addAlbumToPlayList(AddAlbumToPlayListRequest addAlbumToPlayListRequest) {
        this.synchronizedPlayList.addAll(extractTracks(addAlbumToPlayListRequest).orElse(List.of()));
    }

    @Override
    public void stop() {
        log.info("PLAYER -> STOP");
        this.synchronizedPlayList.clearAndStop();
    }

    @Override
    public void pause() {
        log.info("PLAYER -> PAUSE");
        this.mp3SimplePlayerTask.pause();
    }

    @Override
    public void resume() {
        log.info("PLAYER -> RESUME");
        this.mp3SimplePlayerTask.unpause();
    }

    @Override
    public void next() {
        log.info("PLAYER -> NEXT");
        this.synchronizedPlayList.next();
    }

    @Override
    public void previous() {
        log.info("PLAYER -> PREVIOUS");
        this.synchronizedPlayList.previous();
    }

    @Override
    public void volumeUp() {
        this.mp3SimplePlayerTask.volumeUp();
    }

    @Override
    public int getVolume() {
        return this.mp3SimplePlayerTask.volumeValue();
    }

    @Override
    public void volumeDown() {
        this.mp3SimplePlayerTask.volumeDown();
    }

    @Override
    public List<TrackEntity> getPlayListTracks() {
        return this.synchronizedPlayList.fullTrackList();
    }

    private Optional<List<TrackEntity>> extractTracks(AbstractAlbumOrArtistSourceRequest abstractAlbumOrArtistSourceRequest) {

        List<TrackEntity> tracks = new ArrayList<>();

        abstractAlbumOrArtistSourceRequest.withAlbumId()
                .flatMap(albumService::getAlbum)
                .map(AlbumEntity::getTracks)
                .ifPresent(tracks::addAll);

        abstractAlbumOrArtistSourceRequest.withArtistId()
                .map(artistService::getTracksFromArtist)
                .ifPresent(tracks::addAll);

        abstractAlbumOrArtistSourceRequest.withPlayListId()
                .map(playListService::getTracksFromPlayList)
                .ifPresent(tracks::addAll);

        return Optional.of(tracks.stream()
                        .filter(trackEntity -> this.filesFactory.getFileFromBase64Path(trackEntity.getLibraryItemPath()).exists())
                        .toList())
                .filter(Predicate.not(Collection::isEmpty));
    }
}
