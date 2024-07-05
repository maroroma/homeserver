package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateArtistRequest {
    private String artistDirectoryPath;

    private boolean scanAlbums;
}
