package maroroma.homemusicplayer.model.library.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ALBUM")
public class AlbumEntity extends AbstractLibraryEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne @JoinColumn(name = "ARTIST_ID")
    private ArtistEntity artist;

    public AlbumEntity addTrack(TrackEntity trackEntity) {
        if (this.tracks == null) {
            this.tracks = new ArrayList<>();
        }

        trackEntity.setAlbum(this);

        this.tracks.add(trackEntity);

        return this;
    }

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<TrackEntity> tracks;
}
