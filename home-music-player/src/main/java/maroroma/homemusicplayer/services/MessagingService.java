package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.ArtistEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.messaging.api.SimpleBroadcastNotification;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.model.player.api.PlayerStatusEvent;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ArtistMapper  artistMapper;
    private final TrackMapper trackMapper;
    private final AlbumMapper albumMapper;

    public void broadCast(PlayerStatusEvent newPlayerStatusEvent) {
        simpMessagingTemplate.convertAndSend("/topic/player-status", newPlayerStatusEvent);
    }

    @EventListener
    public void broadCast(SimpleBroadcastNotification notification) {
        simpMessagingTemplate.convertAndSend("/topic/notifications", notification);
    }

    public void broadcastStartPlaying(ArtistEntity artistEntity, AlbumEntity albumEntity, TrackEntity trackEntity) {
        this.broadCast(PlayerStatusEvent.builder()
                        .playerStatus(PlayerStatus.PLAYING)
                        .album(this.albumMapper.mapToModel(albumEntity))
                        .artist(this.artistMapper.mapToModel(artistEntity))
                        .track(this.trackMapper.mapToModel(trackEntity))
                .build());
    }

    public void broadcastPausePlaying() {
        this.broadCast(PlayerStatusEvent.builder()
                .playerStatus(PlayerStatus.PAUSED)
                .build());
    }

    public void broadcastResumePlaying() {
        this.broadCast(PlayerStatusEvent.builder()
                .playerStatus(PlayerStatus.PLAYING)
                .build());
    }

    public void broadcastStoppedStatus() {
        simpMessagingTemplate.convertAndSend("/topic/player-stopped", PlayerStatusEvent.builder().playerStatus(PlayerStatus.STOPPED).build());
    }
}
