package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddNewArtistFolderRequest {
    private String artistName;
    private String thumbAsBase64File;
    private String fanartAsBase64File;
}
