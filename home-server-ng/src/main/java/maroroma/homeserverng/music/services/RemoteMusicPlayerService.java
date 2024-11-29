package maroroma.homeserverng.music.services;

import maroroma.homeserverng.music.model.musicplayer.MusicPlayerStatus;
import maroroma.homeserverng.music.model.musicplayer.PlayerStatus;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteMusicPlayerService {

    @Property("homeserver.music.player.url")
    private HomeServerPropertyHolder homeserverMusicPlayerUrl;

    private final SimpleClientHttpRequestFactory simpleClientHttpRequestFactory;

    public RemoteMusicPlayerService() {
        // on raccourci les timeouts pour les appels au player
        this.simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        this.simpleClientHttpRequestFactory.setConnectTimeout(2000);
        this.simpleClientHttpRequestFactory.setReadTimeout(1000);
    }


    public MusicPlayerStatus getPlayerStatus() {
        RestTemplate rt = new RestTemplate(this.simpleClientHttpRequestFactory);

        try {
            return MusicPlayerStatus.builder()
                    .playerStatus(rt.getForObject(this.homeserverMusicPlayerUrl.getResolvedValue() + "/musicplayer/player/status", PlayerStatus.class))
                    .musicPlayerUrl(this.homeserverMusicPlayerUrl.getResolvedValue())
                    .build();

        } catch (Exception e) {
            return MusicPlayerStatus.builder()
                    .playerStatus(PlayerStatus.STOPPED)
                    .musicPlayerUrl(this.homeserverMusicPlayerUrl.getResolvedValue())
                    .build();
        }

    }



}
