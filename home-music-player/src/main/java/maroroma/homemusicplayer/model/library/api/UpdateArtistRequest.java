package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArtistRequest {

    private String newName;
    private boolean autoUpdateArts;

    private boolean updateArtsFromWeb;

    private String fanartUrl;

    private String thumbUrl;

}
