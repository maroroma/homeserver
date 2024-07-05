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
public class Artist extends AbstractLibraryItem {

    private List<UUID> albums;
}
