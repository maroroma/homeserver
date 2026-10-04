package maroroma.homemusicplayer.services.embedded;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.embedded.api.TrackList;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.player.api.AbstractAlbumOrArtistSourceRequest;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;
import maroroma.homemusicplayer.services.AlbumService;
import maroroma.homemusicplayer.services.ArtistService;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.PlayListService;
import maroroma.homemusicplayer.services.caches.TracksCache;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import maroroma.homemusicplayer.tools.CustomAssert;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class EmbeddedPlayerService {

    private final AlbumService albumService;
    private final ArtistService artistService;
    private final PlayListService playListService;
    private final FilesFactory filesFactory;
    private final TrackMapper trackMapper;
    private final TracksCache tracksCache;


    public TrackList generatePlayList(@RequestBody CreatePlayerRequest createPlayerRequest) {
        Assert.notNull(createPlayerRequest, "createPlayerRequest can't be null");

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

        var trackEntities = extractTracks(createPlayerRequest)
                .stream()
                .flatMap(Collection::stream)
                .toList();

        tracksCache.preLoadTrackList(trackEntities);

        var trackApis = trackEntities.stream()
                .map(trackMapper::mapToModel)
                .toList();

        return TrackList.builder().tracks(trackApis).build();
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
