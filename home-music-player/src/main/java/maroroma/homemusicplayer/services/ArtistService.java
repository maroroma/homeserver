package maroroma.homemusicplayer.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.FileAdapterFilter;
import maroroma.homemusicplayer.model.library.api.AddAlbumToArtistRequest;
import maroroma.homemusicplayer.model.library.api.CreateArtistRequest;
import maroroma.homemusicplayer.model.library.api.LibraryItemArts;
import maroroma.homemusicplayer.model.library.api.UpdateArtistRequest;
import maroroma.homemusicplayer.model.library.entities.AlbumEntity;
import maroroma.homemusicplayer.model.library.entities.ArtistEntity;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.repositories.ArtistRepository;
import maroroma.homemusicplayer.tools.CustomAssert;
import maroroma.homemusicplayer.tools.FileUtils;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static maroroma.homemusicplayer.tools.CustomAssert.albumIdNotNull;
import static maroroma.homemusicplayer.tools.CustomAssert.artistIdNotNull;
import static maroroma.homemusicplayer.tools.CustomAssert.fileExists;
import static org.springframework.util.Assert.hasLength;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    private final LibraryItemArtsScrapper libraryItemArtsScrapper;

    private final FilesFactory filesFactory;

    private final AlbumService albumService;

    public List<ArtistEntity> getAllArtists() {
        return this.artistRepository.findAll();
    }

    public Optional<ArtistEntity> getArtist(UUID artistId) {
        return this.artistRepository.findById(artistId);
    }

    public List<ArtistEntity> deleteArtists() {
        StreamUtils.of(this.artistRepository.findAll())
                .map(ArtistEntity::getId)
                .forEach(this::innerDeleteArtist);

        return this.getAllArtists();
    }

    public List<ArtistEntity> deleteArtist(UUID artistId) {
        this.innerDeleteArtist(artistId);

        return this.getAllArtists();
    }


    public List<ArtistEntity> addArtist(CreateArtistRequest createArtistRequest) {

        // données dans la requête
        notNull(createArtistRequest, "createArtistRequest should not be null");

        // pas déjà existant en base
        this.artistRepository.findByLibraryItemPath(createArtistRequest.getArtistDirectoryPath())
                .ifPresent((alreadyExistingArtist) -> {
                    throw new MusicPlayerException(String.format(
                            "Artist <%s> is already integrated in library from directory <%s>",
                            alreadyExistingArtist.getName(),
                            FileUtils.convertBase64ToPath(alreadyExistingArtist.getLibraryItemPath())));
                });

        // répertoire qui existe
        filesFactory.validateBase64PathAsMusicSourceChild(createArtistRequest.getArtistDirectoryPath());
        var artistDirectory = this.filesFactory.getFileFromBase64Path(createArtistRequest.getArtistDirectoryPath());
        CustomAssert.isDirectory(artistDirectory);


        // récup des infos distantes et recopie en local
        LibraryItemArts libraryItemArts = this.libraryItemArtsScrapper.scanAndImportArtistArtsFromArtistDirectory(artistDirectory);


        var artistEntity = new ArtistEntity();
        artistEntity.setName(artistDirectory.getFileName());
        artistEntity.updateArts(libraryItemArts);
        artistEntity.setLibraryItemPath(createArtistRequest.getArtistDirectoryPath());


        if (createArtistRequest.isScanAlbums()) {
            this.scanAndAddNewAlbumsForArtist(artistEntity);
        }
        this.artistRepository.save(artistEntity);


        return this.artistRepository.findAll();
    }

    public List<AlbumEntity> getArtistAlbums(UUID artistId) {
        return this.getArtist(artistId)
                .map(ArtistEntity::getAlbums)
                .orElse(List.of());
    }

    public ArtistEntity scanAndAddNewAlbumsForArtist(UUID artistId) {
        return doWithArtistAndSave(artistId, this::scanAndAddNewAlbumsForArtist);
    }

    public ArtistEntity scanAndAddNewAlbumsForArtist(ArtistEntity artistEntity) {
        // récup des path des albums existants
        var existingAlbumPaths = StreamUtils.of(artistEntity.getAlbums())
                .map(AlbumEntity::getLibraryItemPath)
                .toList();


        // récup des répertoires pas déjà intégrés
        var artistDirectory = this.filesFactory.getFileFromLibraryItem(artistEntity);
        var newAlbumDirectories = artistDirectory.streamFiles()
                .filter(FileAdapterFilter.isDirectory())
                .filter(FileAdapterFilter.base64PathNotIn(existingAlbumPaths))
                .toList();

        // si pas de répertoire, bah rien à scanner
        if (newAlbumDirectories.isEmpty()) {
            return artistEntity;
        }

        var newAlbums = StreamUtils.of(newAlbumDirectories)
                .map(aNewAlbumDirectory -> this.albumService.parseDirectoryForAlbum(artistEntity, aNewAlbumDirectory))
                .toList();

        // back up si thumb pas présent sur artist
        if (ObjectUtils.isEmpty(artistEntity.getThumbPath())) {
            StreamUtils.of(newAlbums)
                    .map(AlbumEntity::getThumbPath)
                    .filter(Predicate.not(ObjectUtils::isEmpty))
                    .findFirst()
                    .ifPresent(artistEntity::setThumbPath);
        }

        newAlbums.forEach(artistEntity::addAlbum);

        return artistEntity;

    }

    @Transactional
    public ArtistEntity removeAlbumFromArtist(UUID artistId, UUID albumId) {
        artistIdNotNull(artistId);
        albumIdNotNull(albumId);

        return this.doWithArtistAndSave(artistId, existingArtist -> {
            StreamUtils.of(existingArtist.getAlbums())
                    .filter(anAlbum -> anAlbum.getId().equals(albumId))
                    .findFirst()
                    .ifPresent(existingAlbumToRemove -> {
                        existingArtist.removeAlbum(existingAlbumToRemove);
                        albumService.deleteOneAlbum(existingAlbumToRemove);
                    });

            return existingArtist;
        });

    }

    public ArtistEntity addAlbumToArtist(UUID artistId, AddAlbumToArtistRequest addAlbumToArtistRequest) {
        artistIdNotNull(artistId);
        notNull(addAlbumToArtistRequest, "addAlbumToArtistRequest can't be null");
        hasLength(addAlbumToArtistRequest.getAlbumToAddPath(), "addAlbumToArtistRequest.getAlbumToAddPath() can't be null or empty");


        this.filesFactory.validateBase64PathAsMusicSourceChild(addAlbumToArtistRequest.getAlbumToAddPath());
        var albumDirectoryToScan = this.filesFactory.getFileFromBase64Path(addAlbumToArtistRequest.getAlbumToAddPath());
        fileExists(albumDirectoryToScan);

        return this.doWithArtistAndSave(artistId, existingArtist -> {
            var newAlbum = this.albumService.parseDirectoryForAlbum(existingArtist, albumDirectoryToScan);
            return existingArtist.addAlbum(newAlbum);
        });

    }


    public ArtistEntity updateArtist(UUID artistId, UpdateArtistRequest updateArtistRequest) {
        artistIdNotNull(artistId);
        notNull(updateArtistRequest, "updateArtistRequest can't be null or empty");

        var updateNameOperation = updateNameOperation(updateArtistRequest);
        var updateArtsOperation = autoUpdateArtsOperation(updateArtistRequest);
        var updateArtsFrowWebOperation = updateArtsFromWebOperation(updateArtistRequest);


        isTrue(updateArtsOperation.isPresent() || updateNameOperation.isPresent() || updateArtsFrowWebOperation.isPresent(), "update should provide at least one update request");

        var operationsToApply = Stream.of(updateArtsFrowWebOperation, updateArtsOperation, updateNameOperation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return this.doWithArtistAndSave(artistId, operationsToApply);

    }

    public List<FileAdapter> getArtistCandidates() {
        var allCandidates = this.filesFactory.listDirectoriesInMusicSource();
        var alreadyIntegratedDirectories = StreamUtils.of(this.getAllArtists())
                .map(ArtistEntity::getLibraryItemPath)
                .map(this.filesFactory::getFileFromBase64Path)
                .toList();

        // on vire les répertoires déjà intégrés
        return StreamUtils.of(allCandidates)
                .filter(aDirectoryCandidate -> alreadyIntegratedDirectories.stream().noneMatch(aDirectoryCandidate::isSameFile))
                .toList();
    }

    public List<FileAdapter> getAlbumCandidates(UUID artistId) {
        artistIdNotNull(artistId);

        return this.artistRepository.findById(artistId)
                .map(this.albumService::getAlbumCandidates)
                .orElseThrow();
    }

    public List<TrackEntity> getTracksFromArtist(UUID artistId) {
        artistIdNotNull(artistId);

        return this.artistRepository.findById(artistId)
                .map(anExistingArtist -> StreamUtils.of(anExistingArtist.getAlbums()))
                .map(albumsForArtist -> albumsForArtist.flatMap(anAlbum -> StreamUtils.of(anAlbum.getTracks())))
                .map(Stream::toList)
                .orElseThrow();
    }

    private Optional<UnaryOperator<ArtistEntity>> updateNameOperation(UpdateArtistRequest updateArtistRequest) {
        return Optional.ofNullable(updateArtistRequest)
                .map(UpdateArtistRequest::getNewName)
                .filter(StringUtils::hasLength)
                .map(aNewName -> artistEntity -> {
                    artistEntity.setName(aNewName);
                    return artistEntity;
                });
    }

    private Optional<UnaryOperator<ArtistEntity>> autoUpdateArtsOperation(UpdateArtistRequest updateArtistRequest) {
        if (!updateArtistRequest.isAutoUpdateArts()) {
            return Optional.empty();
        }


        UnaryOperator<ArtistEntity> operations = artistEntity -> {

            libraryItemArtsScrapper.cleanupLocalArts(artistEntity);
            var newArts = libraryItemArtsScrapper.scanAndImportArtistArtsFromArtistDirectory(artistEntity);
            artistEntity.updateArts(newArts);

            return artistEntity;
        };

        return Optional.of(operations);
    }

    private Optional<UnaryOperator<ArtistEntity>> updateArtsFromWebOperation(UpdateArtistRequest updateArtistRequest) {
        if (!updateArtistRequest.isUpdateArtsFromWeb()) {
            return Optional.empty();
        }

        isTrue(StringUtils.hasLength(updateArtistRequest.getFanartUrl()) || StringUtils.hasLength(updateArtistRequest.getThumbUrl()), "fanart or thumb url should be given");


        UnaryOperator<ArtistEntity> operations = artistEntity -> {

            libraryItemArtsScrapper.cleanupLocalArts(artistEntity);
            var newArts = libraryItemArtsScrapper.scanAndImportArtistArtsFromWeb(updateArtistRequest.getFanartUrl(), updateArtistRequest.getThumbUrl());
            artistEntity.updateArts(newArts);

            return artistEntity;
        };

        return Optional.of(operations);

    }

    private ArtistEntity doWithArtistAndSave(UUID artistId, UnaryOperator<ArtistEntity> actionOnArtistBeforeSaving) {
        artistIdNotNull(artistId);

        return this.artistRepository.findById(artistId)
                .map(actionOnArtistBeforeSaving)
                .map(this.artistRepository::saveAndFlush)
                .orElseThrow();
    }

    private ArtistEntity doWithArtistAndSave(UUID artistId, List<UnaryOperator<ArtistEntity>> actionsOnArtistBeforeSaving) {
        artistIdNotNull(artistId);
        Assert.notEmpty(actionsOnArtistBeforeSaving, "actionsOnArtistBeforeSaving should not be empty");


        return this.artistRepository.findById(artistId)
                .map(anExistingArtist -> actionsOnArtistBeforeSaving.stream()
                        .reduce(
                                anExistingArtist,
                                (previousExistingArtist, operation) -> operation.apply(previousExistingArtist),
                                (ae1, ae2) -> ae1))
                .map(this.artistRepository::saveAndFlush)
                .orElseThrow();
    }

    private void innerDeleteArtist(UUID artistId) {
        artistIdNotNull(artistId);

        this.artistRepository.findById(artistId)
                .ifPresent(artistToDelete -> {

                    // suppression en base
                    this.artistRepository.deleteById(artistId);

                    // suppression des images qui ont été copiées localement pour l'artiste
                    this.libraryItemArtsScrapper.cleanupLocalArts(artistToDelete);

                    // suppression des images qui ont été copiées localement pour les albums
                    StreamUtils.of(artistToDelete.getAlbums())
                            .forEach(this.libraryItemArtsScrapper::cleanupLocalArts);
                });
    }
}
