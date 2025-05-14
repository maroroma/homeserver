package maroroma.homeserverng.music.services;

import maroroma.homeserverng.music.model.musicplayer.MusicPlayerStatus;
import maroroma.homeserverng.music.model.musicplayer.PlayerStatus;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import maroroma.homeserverng.tools.exceptions.Traper;
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

        return Traper.trapWithOptional(() -> rt.getForObject(this.homeserverMusicPlayerUrl.getResolvedValue() + "/musicplayer/player/status/full", MusicPlayerStatus.class))
                .orElse(MusicPlayerStatus.builder()
                        .playerStatus(PlayerStatus.STOPPED).build())
                .toBuilder()
                .musicPlayerUrl(this.homeserverMusicPlayerUrl.getResolvedValue())
                .build();
    }


    public boolean stop() {
        RestTemplate rt = new RestTemplate(this.simpleClientHttpRequestFactory);
        return Traper.trapToBoolean(()  -> rt.delete(this.homeserverMusicPlayerUrl.getResolvedValue() + "/musicplayer/player"));
    }
}
