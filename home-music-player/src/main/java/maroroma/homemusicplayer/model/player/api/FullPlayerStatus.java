package maroroma.homemusicplayer.model.player.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Album;
import maroroma.homemusicplayer.model.library.api.Artist;
import maroroma.homemusicplayer.model.library.api.Track;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullPlayerStatus {

    public static FullPlayerStatus stopped(MemoryStatus memoryStatus) {
        return FullPlayerStatus.builder()
                .playerStatus(PlayerStatus.STOPPED)
                .memoryStatus(memoryStatus)
                .build();
    }

    private PlayerStatus playerStatus;

    private Track track;

    private Artist artist;

    private Album album;

    private int volume;

    private MemoryStatus memoryStatus;
}



