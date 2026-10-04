package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.administration.ApplicationProperty;
import maroroma.homemusicplayer.model.administration.ApplicationStats;
import maroroma.homemusicplayer.model.player.api.MemoryStatus;
import maroroma.homemusicplayer.repositories.AlbumRepository;
import maroroma.homemusicplayer.repositories.ArtistRepository;
import maroroma.homemusicplayer.repositories.TrackRepository;
import maroroma.homemusicplayer.services.caches.TracksCache;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdministrationService {
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final TracksCache tracksCache;

    private final ConfigurableEnvironment configurableEnvironment;

    public List<ApplicationProperty> getApplicationProperties() {

        return configurableEnvironment.getPropertySources().stream()
                .filter(MapPropertySource.class::isInstance)
                .map(MapPropertySource.class::cast)
                .map(MapPropertySource::getPropertyNames)
                .flatMap(Stream::of)
                .map(aPropertyName -> ApplicationProperty.builder()
                        .propertyName(aPropertyName)
                        .propertyValue(configurableEnvironment.getProperty(aPropertyName))
                        .build()
                ).toList();
    }

    public ApplicationStats getApplicationStats() {
        return ApplicationStats.builder()
                .nbTracks(trackRepository.count())
                .nbAlbums(albumRepository.count())
                .nbArtists(artistRepository.count())
                .nbLocalCacheItems(tracksCache.getNbItemsInCache())
                .nbMaxCacheItems(tracksCache.getCacheMaxSize())
                .memoryStatus(generateMemoryStatus())
                .build();
    }

    private MemoryStatus generateMemoryStatus() {
        var freeHeapSize = Runtime.getRuntime().freeMemory();
        var currentHeapSize = Runtime.getRuntime().totalMemory();
        var maxHeapSize = Runtime.getRuntime().maxMemory();
        var percentageUSe = 100.0 * currentHeapSize / maxHeapSize;
        return MemoryStatus.builder()
                .heapFreeSize(freeHeapSize)
                .heapSize(currentHeapSize)
                .heapMaxSize(maxHeapSize)
                .percentageUsedMemory(percentageUSe)
                .build();
    }
}
