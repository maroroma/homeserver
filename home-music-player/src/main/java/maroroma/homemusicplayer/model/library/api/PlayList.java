package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayList {
    private UUID playListId;
    private String name;
    private List<UUID> tracks;

    public PlayList addTrack(UUID trackId) {
        if (this.tracks == null) {
            this.tracks = new ArrayList<>();
        }
        if (!this.tracks.contains(trackId)) {
            this.tracks.add(trackId);
        }
        return this;
    }
}
