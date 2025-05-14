package maroroma.homeserverng.music.model.musicplayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class MusicPlayerStatus {
    private PlayerStatus playerStatus;

    private NamedMusicItem track;

    private NamedMusicItem artist;

    private NamedMusicItem album;

    private int volume;
    private String musicPlayerUrl;
}
