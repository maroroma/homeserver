package maroroma.homemusicplayer.services.mappers.entities;

import maroroma.homemusicplayer.model.library.api.Artist;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.ArtistEntity;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper extends AbstractLibraryItemMapper<Artist, ArtistEntity> {

    @Override
    public Artist mapToModel(ArtistEntity libraryEntity) {
        var artist = new Artist();
        artist.setId(libraryEntity.getId());
        artist.setAlbums(StreamUtils.of(libraryEntity.getAlbums()).map(AlbumEntity::getId).toList());
        return this.basicMapToModel(libraryEntity, artist);
    }

    public Artist lazyMapToModel(ArtistEntity libraryEntity) {
        var artist = new Artist();
        artist.setId(libraryEntity.getId());
        return this.basicMapToModel(libraryEntity, artist);
    }

    @Override
    public ArtistEntity mapToEntity(Artist libraryItem) {
        var artistEntity = new ArtistEntity();
        artistEntity.setId(libraryItem.getId());
        return this.basicMapToEntity(libraryItem, artistEntity);
    }
}
