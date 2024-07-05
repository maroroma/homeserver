package maroroma.homemusicplayer.model.player.api;

import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class AddAlbumToPlayListRequest extends AbstractAlbumOrArtistSourceRequest {
    private UUID albumId;

    private UUID artistId;

}
