package maroroma.homemusicplayer.model.player.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Album;
import maroroma.homemusicplayer.model.library.api.Artist;
import maroroma.homemusicplayer.model.library.api.Track;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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

    private List<String> mp3TaskNames;

}



