package maroroma.homemusicplayer.services.mappers.entities;

import maroroma.homemusicplayer.model.library.api.Album;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper extends AbstractLibraryItemMapper<Album, AlbumEntity> {
    @Override
    public Album mapToModel(AlbumEntity libraryEntity) {
        var album = new Album();
        album.setId(libraryEntity.getId());
        album.setArtistId(libraryEntity.getArtist().getId());
        this.basicMapToModel(libraryEntity, album);
        album.setTracks(StreamUtils.of(libraryEntity.getTracks()).map(TrackEntity::getId).toList());
        return album;
    }

    @Override
    public AlbumEntity mapToEntity(Album libraryItem) {
        return null;
    }
}
