package maroroma.homemusicplayer.model.library.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Track extends AbstractLibraryItem {
    private String trackNumber;
    private int durationInSeconds;

    private UUID albumId;
}
