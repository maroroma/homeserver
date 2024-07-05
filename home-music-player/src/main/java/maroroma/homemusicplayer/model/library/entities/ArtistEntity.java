package maroroma.homemusicplayer.model.library.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "ARTIST")
public class ArtistEntity extends AbstractLibraryEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<AlbumEntity> albums;

    public ArtistEntity addAlbum(AlbumEntity entity) {
        if (this.getAlbums() == null) {
            this.setAlbums(new ArrayList<>());
        }

        this.getAlbums().add(entity);
        entity.setArtist(this);

        return this;
    }

    public ArtistEntity removeAlbum(AlbumEntity entity) {
        this.getAlbums().remove(entity);
        return this;
    }


}
