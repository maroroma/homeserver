package maroroma.homemusicplayer.model.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homemusicplayer.model.player.api.MemoryStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStats {
    private MemoryStatus memoryStatus;
    private long nbArtists;
    private long nbAlbums;
    private long nbTracks;
    private int nbLocalCacheItems;
    private int nbMaxCacheItems;
}
