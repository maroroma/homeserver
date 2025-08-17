package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.player.api.FullPlayerStatus;
import maroroma.homemusicplayer.model.player.api.MemoryStatus;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import maroroma.homemusicplayer.tools.Traper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerMonitor {

    private final PlayerService playerService;

    private final ArtistMapper artistMapper;
    private final TrackMapper trackMapper;
    private final AlbumMapper albumMapper;


    public FullPlayerStatus generatePlayerStatus() {
        if (this.playerService.getPlayerStatus() == PlayerStatus.STOPPED) {
            return FullPlayerStatus.stopped(generateMemoryStatus());
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
                                .build();
                    })).orElse(FullPlayerStatus.stopped(generateMemoryStatus()));

        }
    }

    private MemoryStatus generateMemoryStatus() {
        return MemoryStatus.builder()
                .heapFreeSize(Runtime.getRuntime().freeMemory())
                .heapSize(Runtime.getRuntime().totalMemory())
                .heapMaxSize(Runtime.getRuntime().maxMemory())
                .build();
    }

}
