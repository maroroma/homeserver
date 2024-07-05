package maroroma.homemusicplayer.model.player.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Data
public abstract class AbstractAlbumOrArtistSourceRequest {

    private UUID albumId;

    private UUID artistId;


    public Optional<UUID> withAlbumId() {
        return Optional.ofNullable(this.getAlbumId());
    }

    public Optional<UUID> withArtistId() {
        return Optional.ofNullable(this.getArtistId());
    }
}
