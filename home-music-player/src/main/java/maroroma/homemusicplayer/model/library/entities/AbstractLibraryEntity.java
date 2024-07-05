package maroroma.homemusicplayer.model.library.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.LibraryItemArts;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractLibraryEntity {


    @Column(name = "ITEM_NAME")
    private String name;

    @Column(name = "THUMB_PATH")
    private String thumbPath;

    @Column(name = "FANART_PATH")
    private String fanartPath;

    @Column(name = "LIBRARY_ITEM_PATH")
    private String libraryItemPath;


    public void updateArts(LibraryItemArts libraryItemArts) {
        this.fanartPath = libraryItemArts.encodedFanartPath();
        this.thumbPath = libraryItemArts.encodedThumbPath();
    }

}
