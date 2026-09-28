package maroroma.homemusicplayer.services.mappers.entities;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.services.FilesFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackMapper extends AbstractLibraryItemMapper<Track, TrackEntity> {

    private final FilesFactory filesFactory;

    @Override
    public Track mapToModel(TrackEntity libraryEntity) {
        Track track = new Track();
        track.setTrackNumber(libraryEntity.getTrackNumber());
//        track.setName(libraryEntity.getName());
        track.setDurationInSeconds(libraryEntity.getDurationInSeconds());
        track.setId(libraryEntity.getId());
        track.setAlbumId(libraryEntity.getAlbum().getId());
        track.setShortFileName(filesFactory.getFileFromBase64Path(libraryEntity.getLibraryItemPath()).getFileName());
        this.basicMapToModel(libraryEntity, track);
        return track;
    }

    @Override
    public TrackEntity mapToEntity(Track libraryItem) {
        return null;
    }
}
