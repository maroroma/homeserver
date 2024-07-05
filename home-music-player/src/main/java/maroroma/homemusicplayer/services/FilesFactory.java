package maroroma.homemusicplayer.services;


import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.FileAdapterFilter;
import maroroma.homemusicplayer.model.files.LocalFileAdapter;
import maroroma.homemusicplayer.model.files.SmbFileAdapter;
import maroroma.homemusicplayer.model.library.entities.AbstractLibraryEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

import static maroroma.homemusicplayer.tools.FileUtils.convertBase64ToPath;

@Component
public class FilesFactory {

    private final SmbFileAdapter.SambaUserSupplier sambaUserSupplier;

    private final String musicSource;
    private final String thumbsSource;
    private final String fanartsSource;

    public FilesFactory(
            SmbFileAdapter.SambaUserSupplier sambaUserSupplier,
            @Value("${musicplayer.music-source}") String musicSource,
            @Value("${musicplayer.localresources.images.artist.thumbs}") String thumbsSource,
            @Value("${musicplayer.localresources.images.artist.fanarts}") String fanartsSource) {
        this.sambaUserSupplier = sambaUserSupplier;
        this.musicSource = musicSource;
        this.thumbsSource = thumbsSource;
        this.fanartsSource = fanartsSource;
    }


    public FileAdapter getFileFromBase64Path(String base64Path) {
        Assert.hasLength(base64Path, "base64Path should not be null or empty");
        var convertedPath = convertBase64ToPath(base64Path);
        return this.getFileFromPath(convertedPath);
    }

    public FileAdapter getFileFromPath(String path) {
        Assert.hasLength(path, "path should not be null or empty");
        if (path.startsWith("smb://")) {
            return new SmbFileAdapter(path, this.sambaUserSupplier);
        } else {
            return new LocalFileAdapter(path);
        }
    }

    public FileAdapter getFileFromLibraryItem(AbstractLibraryEntity abstractLibraryEntity) {
        return getFileFromBase64Path(abstractLibraryEntity.getLibraryItemPath());
    }

    public void validateBase64PathAsMusicSourceChild(String base64Path) {
        var rootFileAdapter = this.getFileFromPath(this.musicSource);
        var filePathToValidate = this.getFileFromBase64Path(base64Path);

        Assert.isTrue(filePathToValidate.getResolvedPath().startsWith(rootFileAdapter.getResolvedPath()), "filePathToValidate doesn't belong to music source");
    }

    public FileAdapter validateBase64PathAsThumbSourceChild(String base64Path) {
        return this.validateAsChildForSource(this.thumbsSource, base64Path);
    }

    public FileAdapter validateBase64PathAsFanartSourceChild(String base64Path) {
        return this.validateAsChildForSource(this.fanartsSource, base64Path);
    }

    public List<FileAdapter> listDirectoriesInMusicSource() {
        return this.getFileFromPath(this.musicSource).streamFiles()
                .filter(FileAdapterFilter.isDirectory())
                .toList();
    }

    private FileAdapter validateAsChildForSource(String source, String base64PathToValidate) {
        var sourceFileAdapter = this.getFileFromPath(source);
        var filePathToValidate = this.getFileFromBase64Path(base64PathToValidate);

        Assert.isTrue(filePathToValidate.getResolvedPath().startsWith(sourceFileAdapter.getResolvedPath()), "filePathToValidate doesn't belong to given source");

        return filePathToValidate;

    }

}
