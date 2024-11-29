package maroroma.homeserverng.music.model.musicplayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MusicPlayerStatus {
    private PlayerStatus playerStatus;
    private String musicPlayerUrl;
}
