package maroroma.homemusicplayer.services;

import maroroma.homemusicplayer.model.files.FileAdapterFilter;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.repositories.TrackRepository;
import maroroma.homemusicplayer.services.mp3.tags.Mp3TagReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import static maroroma.homemusicplayer.tools.CustomAssert.isDirectory;

@Service
public class TrackService {


    private final List<String> supportedMusicExtensions;

    private final TrackRepository trackRepository;

    private final FilesFactory filesFactory;

    private final Mp3TagReader mp3TagReader;



    public TrackService(@Value("${musicplayer.music.supported-extensions}") List<String> supportedMusicExtensions,
                        TrackRepository trackRepository,
                        FilesFactory filesFactory,
                        Mp3TagReader mp3TagReader) {
        this.supportedMusicExtensions = supportedMusicExtensions;
        this.trackRepository = trackRepository;
        this.filesFactory = filesFactory;
        this.mp3TagReader = mp3TagReader;
    }

    public List<TrackEntity> findTracksForAlbum(AlbumEntity entity) {
        return this.trackRepository.findAllByAlbum(entity);
    }

    public void deleteTracksForAlbum(AlbumEntity entity) {
        this.trackRepository.deleteByAlbum(entity);
    }


    public List<TrackEntity> scanDirectoryForTracks(AlbumEntity albumEntity) {
        var artistDirectory = this.filesFactory.getFileFromLibraryItem(albumEntity);

        isDirectory(artistDirectory);

        return artistDirectory.streamFiles()
                .filter(FileAdapterFilter.extensionIn(this.supportedMusicExtensions))
                .map(aMusicFile -> {
                    var trackEntity = new TrackEntity();
                    trackEntity.setAlbum(albumEntity);


                    var tags = mp3TagReader.extractTags(aMusicFile);

                    Optional.ofNullable(tags.get(Mp3TagReader.Mp3Tags.TITLE))
                            .map(Mp3TagReader.TagReadingResult::getValue)
                            .ifPresentOrElse(trackEntity::setName, () -> trackEntity.setName(aMusicFile.getFileName()));
                    Optional.ofNullable(tags.get(Mp3TagReader.Mp3Tags.TRACK_NUMBER))
                            .map(Mp3TagReader.TagReadingResult::getValue)
                            .ifPresent(trackEntity::setTrackNumber);


                    trackEntity.setLibraryItemPath(aMusicFile.pathAsBase64());
                    return trackEntity;
                })
                .toList();


    }


}
