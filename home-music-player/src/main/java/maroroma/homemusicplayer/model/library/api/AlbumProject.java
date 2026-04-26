package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AlbumProject {
    private UUID projectId;
    private UUID artistId;
    private String albumName;
    private String albumArtBase64Path;
    private String projectPath;
    private String albumDirectoryOnMusicSource;
}
