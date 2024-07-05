package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.model.player.api.PlayerStatusEvent;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerMonitor {

    private final PlayerService playerService;
    private final MessagingService messagingService;

    private final ArtistMapper artistMapper;
    private final TrackMapper trackMapper;
    private final AlbumMapper albumMapper;


    @Scheduled(fixedRate = 500 )
    public void monitoringPlayer() {
        if (this.playerService.getPlayerStatus() == PlayerStatus.STOPPED) {
            this.messagingService.broadcastStoppedStatus();
        } else {
            try {

                this.playerService.getPlayList()
                        .getOptionalCurrentTrack()
                        .ifPresent(currentTrack -> {
                            var currentAlbum = currentTrack.getAlbum();
                            var currentArtist = currentAlbum.getArtist();

                            this.messagingService.broadCast(PlayerStatusEvent.builder()
                                    .playerStatus(this.playerService.getPlayerStatus())
                                    .track(trackMapper.mapToModel(currentTrack))
                                    .artist(artistMapper.lazyMapToModel(currentArtist))
                                    .album(albumMapper.mapToModel(currentAlbum))
                                    .volume(this.playerService.getVolume())
                                    .build());
                        });

            } catch(Exception e) {
                System.out.println(e);
//                this.playerService.stop();
            }
        }
    }

}
