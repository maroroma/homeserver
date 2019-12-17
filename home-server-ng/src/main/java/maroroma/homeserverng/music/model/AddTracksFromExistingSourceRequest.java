package maroroma.homeserverng.music.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTracksFromExistingSourceRequest {
    List<String> fileIdsForWorkingDirectory;
}
