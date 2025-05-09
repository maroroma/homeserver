package maroroma.homemusicplayer.model.library.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import maroroma.homemusicplayer.model.files.FileAdapter;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "TRACK")
public class TrackEntity extends AbstractLibraryEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "TRACK_NUMBER")
    private String trackNumber;

    @Column(name = "DURATION")
    private int durationInSeconds;

    @Column(name = "ITEM_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "ALBUM_ID")
    private AlbumEntity album;

    public TrackEntity replaceLibraryItemPath(FileAdapter newFileAdapter) {
        return this.toBuilder().libraryItemPath(newFileAdapter.pathAsBase64()).build();
    }

}
