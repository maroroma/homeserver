package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.FileAdapterFilter;
import maroroma.homemusicplayer.model.library.api.LibraryItemArts;
import maroroma.homemusicplayer.model.library.entities.AbstractLibraryEntity;
import maroroma.homemusicplayer.model.library.entities.ArtistEntity;

import java.util.*;

@RequiredArgsConstructor
public class LibraryItemArtsScrapper {

    private final FilesFactory filesFactory;
    private final List<String> supportedThumbNames;
    private final List<String> supportedFanartNames;
    private final List<String> supportedImagesExtensions;
    private final String thumbPath;
    private final String fanartPath;


    public void cleanupLocalArts(AbstractLibraryEntity abstractLibraryEntity) {
        try {
            Optional.ofNullable(abstractLibraryEntity.getFanartPath())
                    .map(filesFactory::getFileFromBase64Path)
                    .ifPresent(FileAdapter::delete);

            Optional.ofNullable(abstractLibraryEntity.getThumbPath())
                    .map(filesFactory::getFileFromBase64Path)
                    .ifPresent(FileAdapter::delete);
        } catch (Exception e) {}
    }

    public LibraryItemArts scanAndImportAlbumArt(FileAdapter albumDirectory) {
        return this.getFileAndImportLocally(albumDirectory, this.supportedThumbNames, this.thumbPath)
                .map(aImportedThumbPath -> LibraryItemArts.builder().thumbPath(aImportedThumbPath).build())
                .orElse(LibraryItemArts.builder().build());
    }

    public LibraryItemArts scanAndImportArtistArtsFromArtistDirectory(ArtistEntity artist) {
        return scanAndImportArtistArtsFromArtistDirectory(filesFactory.getFileFromLibraryItem(artist));
    }

    public LibraryItemArts scanAndImportArtistArtsFromArtistDirectory(FileAdapter artistDirectory) {

        var libraryItemArtsBuilder = LibraryItemArts.builder();

        this.getFileAndImportLocally(artistDirectory, this.supportedFanartNames, this.fanartPath)
                .ifPresent(libraryItemArtsBuilder::fanartPath);

        this.getFileAndImportLocally(artistDirectory, this.supportedThumbNames, this.thumbPath)
                .ifPresent(libraryItemArtsBuilder::thumbPath);

        return libraryItemArtsBuilder.build();

    }

    public LibraryItemArts scanAndImportArtistArtsFromWeb(String fanartUrl, String thumbUrl) {
        var libraryItemArtsBuilder = LibraryItemArts.builder();

        return libraryItemArtsBuilder.build();

    }



    private Optional<String> getFileAndImportLocally(FileAdapter directoryToScan,
                                                     List<String> supportedFileNames,
                                                     String localImportPath) {
        return directoryToScan.streamFiles()
                .filter(FileAdapterFilter.extensionIn(this.supportedImagesExtensions))
                .filter(FileAdapterFilter.nameStartingBy(supportedFileNames))
                .findFirst()
                .map(remoteFileAdapter -> {
                    var fanartDestination = this.filesFactory.getFileFromPath(localImportPath).combine(UUID.randomUUID().toString());
                    remoteFileAdapter.copyTo(fanartDestination);
                    return fanartDestination;
                })
                .map(FileAdapter::getResolvedPath);
    }
}
