package maroroma.homemusicplayer.model.player.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
public class CreatePlayerRequest extends AbstractAlbumOrArtistSourceRequest {

    @Getter
    private UUID trackId;
}
