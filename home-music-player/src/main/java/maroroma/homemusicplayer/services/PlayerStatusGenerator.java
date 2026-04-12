package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.player.api.FullPlayerStatus;
import maroroma.homemusicplayer.model.player.api.MemoryStatus;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import maroroma.homemusicplayer.services.mp3.InputStreamManager;
import maroroma.homemusicplayer.services.mp3.Mp3SimplePlayerTask;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerStatusGenerator {

    private final SynchronizedPlayList synchronizedPlayList;
    private final Mp3SimplePlayerTask mp3SimplePlayerTask;
    private final InputStreamManager inputStreamManager;

    private final ArtistMapper artistMapper;
    private final TrackMapper trackMapper;
    private final AlbumMapper albumMapper;

    public FullPlayerStatus generatePlayerStatus() {
        if (this.synchronizedPlayList.isEmpty()) {
            return FullPlayerStatus.stopped(generateMemoryStatus()).toBuilder()
                    .build();
        } else {

            return Traper.trap(() -> this.synchronizedPlayList
                    .getOptionalCurrentTrack()
                    .map(currentTrack -> {
                        var currentAlbum = currentTrack.getAlbum();
                        var currentArtist = currentAlbum.getArtist();

                        return FullPlayerStatus.builder()
                                .playerStatus(resolvePlayerStatus())
                                .track(trackMapper.mapToModel(currentTrack))
                                .artist(artistMapper.lazyMapToModel(currentArtist))
                                .album(albumMapper.lazyMapToModel(currentAlbum))
                                .volume(this.mp3SimplePlayerTask.volumeValue())
                                .memoryStatus(generateMemoryStatus())
                                .build();
                    })).orElse(FullPlayerStatus.stopped(generateMemoryStatus()));

        }
    }

    public PlayerStatus resolvePlayerStatus() {
        if (this.synchronizedPlayList.isEmpty()) {
            return PlayerStatus.STOPPED;
        } else if (this.inputStreamManager.isLoadingCurrentTrack()) {
            return PlayerStatus.LOADING;
        } else if (this.mp3SimplePlayerTask.isPaused()) {
            return PlayerStatus.PAUSED;
        } else {
            return PlayerStatus.PLAYING;
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
                .build();
    }

}
