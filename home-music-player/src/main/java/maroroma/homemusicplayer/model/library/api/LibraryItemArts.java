package maroroma.homemusicplayer.model.library.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homemusicplayer.tools.FileUtils;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LibraryItemArts {

    private String thumbPath;

    private String fanartPath;

    @JsonIgnore
    public String encodedThumbPath() {
        return Optional.ofNullable(this.thumbPath)
                .map(FileUtils::convertPathToBase64)
                .orElse(null);
    }

    @JsonIgnore
    public String encodedFanartPath() {
        return Optional.ofNullable(this.fanartPath)
                .map(FileUtils::convertPathToBase64)
                .orElse(null);
    }
}
