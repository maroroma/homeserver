package maroroma.homemusicplayer.services.mappers.entities;

import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import org.springframework.stereotype.Component;

@Component
public class TrackMapper extends AbstractLibraryItemMapper<Track, TrackEntity> {
    @Override
    public Track mapToModel(TrackEntity libraryEntity) {
        Track track = new Track();
        track.setTrackNumber(libraryEntity.getTrackNumber());
//        track.setName(libraryEntity.getName());
        track.setDurationInSeconds(libraryEntity.getDurationInSeconds());
        track.setId(libraryEntity.getId());
        track.setAlbumId(libraryEntity.getAlbum().getId());
        this.basicMapToModel(libraryEntity, track);
        return track;
    }

    @Override
    public TrackEntity mapToEntity(Track libraryItem) {
        return null;
    }
}
