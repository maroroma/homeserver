package maroroma.homemusicplayer.services;

import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.FileAdapterFilter;
import maroroma.homemusicplayer.model.library.api.LibraryItemArts;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.ArtistEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.repositories.AlbumRepository;
import maroroma.homemusicplayer.tools.CustomAssert;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.*;

@Service
public class AlbumService {

    private static final String REDUDANT_ARTIST_NAME_REGEX = "^(.+) - (.+)";

    private final LibraryItemArtsScrapper libraryItemArtsScrapper;

    private final AlbumRepository albumRepository;

    private final TrackService trackService;

    private final FilesFactory filesFactory;

    private final List<String> albumNamingExceptions;

    public AlbumService(LibraryItemArtsScrapper libraryItemArtsScrapper,
                        AlbumRepository albumRepository,
                        TrackService trackService,
                        FilesFactory filesFactory,
                        @Value("${musicplayer.music.album-naming-exception}") List<String> albumNamingExceptions) {
        this.libraryItemArtsScrapper = libraryItemArtsScrapper;
        this.albumRepository = albumRepository;
        this.trackService = trackService;
        this.filesFactory = filesFactory;
        this.albumNamingExceptions = albumNamingExceptions;
    }

    public List<AlbumEntity> getAllAlbums() {
        return this.albumRepository.findAll();
    }

    public Optional<AlbumEntity> getAlbum(UUID albumId) {
        return this.albumRepository.findById(albumId);
    }

    public List<TrackEntity> findTracksForAlbum(UUID albumId) {
        return this.albumRepository.findById(albumId)
                .map(this.trackService::findTracksForAlbum)
                .orElseThrow();
    }

    public void deleteOneAlbum(AlbumEntity existingAlbumToRemove) {
        libraryItemArtsScrapper.cleanupLocalArts(existingAlbumToRemove);
        this.trackService.deleteTracksForAlbum(existingAlbumToRemove);
        this.albumRepository.deleteById(existingAlbumToRemove.getId());
    }

    public List<FileAdapter> getAlbumCandidates(ArtistEntity artistEntity) {
        var artistDirectory = this.filesFactory.getFileFromLibraryItem(artistEntity);
        var allCandidates = artistDirectory.streamFiles()
                .filter(FileAdapterFilter.isDirectory())
                .toList();



        var alreadyIntegratedDirectories = StreamUtils.of(artistEntity.getAlbums())
                .map(AlbumEntity::getLibraryItemPath)
                .map(this.filesFactory::getFileFromBase64Path)
                .toList();

        // on vire les répertoires déjà intégrés
        return StreamUtils.of(allCandidates)
                .filter(aDirectoryCandidate -> alreadyIntegratedDirectories.stream().noneMatch(aDirectoryCandidate::isSameFile))
                .toList();
    }


    public AlbumEntity parseDirectoryForAlbum(ArtistEntity artistEntity, FileAdapter albumDirectory) {
        CustomAssert.isDirectory(albumDirectory);

        // parsing de l'album
        var albumEntity = new AlbumEntity();
        albumEntity.setName(resolveAlbumName(artistEntity, albumDirectory));
        albumEntity.setLibraryItemPath(albumDirectory.pathAsBase64());
        // récup des infos distantes et recopie en local
        LibraryItemArts libraryItemArts = this.libraryItemArtsScrapper.scanAndImportAlbumArt(albumDirectory);
        albumEntity.setThumbPath(libraryItemArts.encodedThumbPath());
        albumEntity.setArtist(artistEntity);

        // parsing des tracks
        var tracks = trackService.scanDirectoryForTracks(albumEntity);

        tracks.forEach(albumEntity::addTrack);

        return albumEntity;
    }

    /**
     * Si le nom de l'artiste est présent dans le nom de l'album on le vire
     * @param artist
     * @param albumDirectory
     * @return
     */
    private String resolveAlbumName(ArtistEntity artist, FileAdapter albumDirectory) {

        // si le nom ne doit pas être transformé, on le retourne tel quel
        if (this.albumNamingExceptions.stream().anyMatch(anException -> albumDirectory.getFileName().toLowerCase().contains(anException.toLowerCase()))) {
            return albumDirectory.getFileName();
        }


        Matcher matcher = Pattern.compile(REDUDANT_ARTIST_NAME_REGEX).matcher(albumDirectory.getFileName());

        if (matcher.matches()) {
            return matcher.group(2);
        } else {
            return albumDirectory.getFileName();
        }

    }

}
