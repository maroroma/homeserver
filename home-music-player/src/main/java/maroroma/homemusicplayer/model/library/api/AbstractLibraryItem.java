package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class AbstractLibraryItem {

    private UUID id;

    private String name;


    private LibraryItemArts libraryItemArts;

    private String libraryItemPath;

}
