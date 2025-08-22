package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.player.api.FullPlayerStatus;
import maroroma.homemusicplayer.model.player.api.MemoryStatus;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.caches.InputStreamCache;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import maroroma.homemusicplayer.services.mp3.Mp3Player;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerMonitor {

    private final PlayerService playerService;
    private final Mp3Player mp3Player;

    private final ArtistMapper artistMapper;
    private final TrackMapper trackMapper;
    private final AlbumMapper albumMapper;
    private final InputStreamCache streamCache;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void test() {
        log.info("start scheduled cleanOversizedCache");
        streamCache.cleanOversizedCache();
        log.info("stop scheduled cleanOversizedCache");
    }


    public FullPlayerStatus generatePlayerStatus() {
        if (this.playerService.getPlayerStatus() == PlayerStatus.STOPPED) {
            return FullPlayerStatus.stopped(generateMemoryStatus()).toBuilder()
                    .mp3TaskNames(mp3Player.getTaskNames())
                    .build();
        } else {

            return Traper.trap(() -> this.playerService.getPlayList()
                    .getOptionalCurrentTrack()
                    .map(currentTrack -> {
                        var currentAlbum = currentTrack.getAlbum();
                        var currentArtist = currentAlbum.getArtist();

                        return FullPlayerStatus.builder()
                                .playerStatus(this.playerService.getPlayerStatus())
                                .track(trackMapper.mapToModel(currentTrack))
                                .artist(artistMapper.lazyMapToModel(currentArtist))
                                .album(albumMapper.mapToModel(currentAlbum))
                                .volume(this.playerService.getVolume())
                                .memoryStatus(generateMemoryStatus())
                                .mp3TaskNames(mp3Player.getTaskNames())
                                .build();
                    })).orElse(FullPlayerStatus.stopped(generateMemoryStatus()));

        }
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
                .memoryCacheSize(this.streamCache.getCacheCurrentSize())
                .build();
    }

}
