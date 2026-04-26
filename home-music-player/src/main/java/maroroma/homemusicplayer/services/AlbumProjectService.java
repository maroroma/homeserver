package maroroma.homemusicplayer.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.files.LocalFileAdapter;
import maroroma.homemusicplayer.model.library.api.AlbumProject;
import maroroma.homemusicplayer.model.library.api.CreateAlbumProjectRequest;
import maroroma.homemusicplayer.model.library.api.RenameOneFileRequest;
import maroroma.homemusicplayer.model.library.api.TargetedFileByName;
import maroroma.homemusicplayer.model.library.entities.AbstractLibraryEntity;
import maroroma.homemusicplayer.model.upload.UploadFileStream;
import maroroma.homemusicplayer.services.mp3.tags.FileNameTagParser;
import maroroma.homemusicplayer.services.mp3.tags.Mp3TagReader;
import maroroma.homemusicplayer.tools.CustomAssert;
import maroroma.homemusicplayer.tools.CustomObjectMapper;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import maroroma.homemusicplayer.tools.Traper;
import org.apache.commons.lang3.text.WordUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumProjectService {
    private final ArtistService artistService;
    private final FilesFactory filesFactory;
    private final UploadResourcesService uploadResourcesService;
    private final CustomObjectMapper customObjectMapper;
    private final FileNameTagParser fileNameTagParser;
    private final AlbumService albumService;

    public AlbumProject getAlbumProject(UUID albumProjectId) {

        var projectFile = resolveProjectFile(albumProjectId);
        CustomAssert.fileExists(projectFile);

        return customObjectMapper.read(AlbumProject.class, projectFile);
    }

    public boolean deleteAllAlbumProjects() {
        this.filesFactory.albumProjectDirectory()
                .getFiles()
                .forEach(FileAdapter::delete);

        return true;
    }

    public AlbumProject deleteAlbumProject(UUID albumProjectId) {

        var albumProject = getAlbumProject(albumProjectId);

        this.filesFactory.getFileFromBase64Path(albumProject.getProjectPath())
                .delete();

        resolveProjectFile(albumProjectId).delete();

        return albumProject;
    }

    public AlbumProject startAlbumProjectFromExistingAlbum(UUID albumId) {
        var existingAlbum = albumService.getAlbum(albumId).orElseThrow();

        var projectID = UUID.randomUUID();

        var projectDirectory = filesFactory.albumProjectDirectory()
                .combine(projectID.toString())
                .mkdirs();

        var albumProject = AlbumProject.builder()
                .projectId(projectID)
                .projectPath(projectDirectory.pathAsBase64())
                .albumName(existingAlbum.getName())
                .albumId(existingAlbum.getId())
                .artistId(existingAlbum.getArtist().getId())
                .albumDirectoryOnMusicSource(existingAlbum.getLibraryItemPath())
                .fromExistingAlbum(true)
                .build();

        return customObjectMapper.save(albumProject, resolveProjectFile(projectID));
    }

    public AlbumProject startAlbumProject(CreateAlbumProjectRequest createAlbumProjectRequest) {

        var artistEntity = artistService.getArtist(createAlbumProjectRequest.getArtistId())
                .orElseThrow(() -> new MusicPlayerException("can't find artist"));

        var projectID = UUID.randomUUID();

        var projectDirectory = filesFactory.albumProjectDirectory()
                .combine(projectID.toString())
                .mkdirs();

        var albumArt = uploadResourcesService.uploadThumb(projectDirectory, createAlbumProjectRequest.getAlbumArtAsBase64File());

        var projectToSave = AlbumProject.builder()
                .projectId(projectID)
                .artistId(artistEntity.getId())
                .albumName(WordUtils.capitalize(createAlbumProjectRequest.getAlbumName()))
                .projectPath(projectDirectory.pathAsBase64())
                .albumArtBase64Path(albumArt.pathAsBase64())
                .build();

        return customObjectMapper.save(projectToSave, resolveProjectFile(projectID));
    }

    public AlbumProject addNewFilesToProject(UUID projectId, final HttpServletRequest request) {
        var albumProject = getAlbumProject(projectId);

        UploadFileStream.fromRequest(request)
                .foreach(oneFile ->
                        this.filesFactory
                                .getFileFromBase64Path(albumProject.getProjectPath())
                                .combine(oneFile.getFileName())
                                .copyFrom(oneFile.getInputStream())
                );

        return albumProject;
    }

    public AlbumProject renameOneFile(UUID projectId, RenameOneFileRequest renameOneFileRequest) {
        var albumProject = getAlbumProject(projectId);

        var initialFile = this.filesFactory.getFileFromBase64Path(albumProject.getProjectPath())
                .combine(renameOneFileRequest.getInitialFileName());

        CustomAssert.fileExists(initialFile);

        initialFile.rename(renameOneFileRequest.getNewFileName());
        return albumProject;
    }

    public AlbumProject applyMp3Tags(UUID projectId, TargetedFileByName fileToApplyTagsTo) {
        var albumProject = getAlbumProject(projectId);


        var initialFile = this.filesFactory.getFileFromBase64Path(albumProject.getProjectPath())
                .combine(fileToApplyTagsTo.getFileName());

        CustomAssert.fileExists(initialFile);

        // les libs ne sont pas compatible avec smbnfile, et ne permettent pas de jouer avec un input/output stream
        if (initialFile instanceof LocalFileAdapter localFileAdapter) {
            var localFile = localFileAdapter.getLocalFile();

            var tags = fileNameTagParser.extractTags(localFileAdapter);
            try {
                AudioFile f = AudioFileIO.read(localFile);

                var artist = this.artistService.getArtist(albumProject.getArtistId()).orElseThrow();

                Tag tag = f.getTag();

                cleanPreviousTags(tag);

                var newArtwork = createArtWork(albumProject);
                if (newArtwork.isPresent()) {
                    tag.setField(newArtwork.get());
                }


                tag.setField(FieldKey.ARTIST, WordUtils.capitalize(artist.getName()));
                tag.setField(FieldKey.ALBUM, WordUtils.capitalize(albumProject.getAlbumName()));
                tag.setField(FieldKey.TITLE, WordUtils.capitalize(tags.get(Mp3TagReader.Mp3Tags.TITLE).getValue()));

                if (tags.get(Mp3TagReader.Mp3Tags.TRACK_NUMBER) != null) {
                    tag.setField(FieldKey.TRACK, tags.get(Mp3TagReader.Mp3Tags.TRACK_NUMBER).getValue());
                }

                f.commit();
            } catch (Exception e) {
                throw new MusicPlayerException(e);
            }
        }

        return albumProject;
    }

    public AlbumProject createAlbumOnMusicSource(UUID projectId) {
        var albumProject = getAlbumProject(projectId);
        var artist = this.artistService.getArtist(albumProject.getArtistId()).orElseThrow();

        // création du répertoire
        var albumDirectoryOnMusicSource = this.filesFactory.getFileFromBase64Path(artist.getLibraryItemPath())
                .combine(artist.getName() + " - " + albumProject.getAlbumName())
                .mkdirs();


        // recopie de l'albumart
        var projectAlbumArt = this.filesFactory.getFileFromBase64Path(albumProject.getAlbumArtBase64Path());
        var remoteAlbumArt = albumDirectoryOnMusicSource.combine(projectAlbumArt.getFileName());

        projectAlbumArt.copyTo(remoteAlbumArt);

        // mise à jour du project
        return customObjectMapper.save(
                albumProject.toBuilder()
                        .albumDirectoryOnMusicSource(albumDirectoryOnMusicSource.pathAsBase64())
                        .build()
                , resolveProjectFile(albumProject.getProjectId()));
    }

    public AlbumProject copyTrackToMusicSource(UUID projectId, TargetedFileByName fileToApplyTagsTo) {
        var albumProject = getAlbumProject(projectId);

        var albumDirectoryOnMusicSource = this.filesFactory.getFileFromBase64Path(albumProject.getAlbumDirectoryOnMusicSource());
        CustomAssert.fileExists(albumDirectoryOnMusicSource);

        var trackToCopyOnMusicSource = this.filesFactory.getFileFromBase64Path(albumProject.getProjectPath())
                .combine(fileToApplyTagsTo.getFileName());
        CustomAssert.fileExists(trackToCopyOnMusicSource);

        var trackTarget = albumDirectoryOnMusicSource.combine(WordUtils.capitalize(trackToCopyOnMusicSource.getFileName()));

        trackToCopyOnMusicSource.copyTo(trackTarget);

        return albumProject;
    }

    private void cleanPreviousTags(Tag tag) {
        // on vire tous les anciens tags, si présents
        Stream.of(FieldKey.values())
                .filter(key -> tag.hasField(key.name()))
                .forEach(tag::deleteField);

        Traper.trapToBoolean(tag::deleteArtworkField);
    }

    private Optional<Artwork> createArtWork(AlbumProject albumProject) {
        FileAdapter albumArtFromProject;
        if (albumProject.isFromExistingAlbum()) {
            albumArtFromProject = this.albumService.getAlbum(albumProject.getAlbumId())
                    .map(AbstractLibraryEntity::getThumbPath)
                    .map(this.filesFactory::getFileFromBase64Path)
                    .orElseThrow();
        } else {
            albumArtFromProject = this.filesFactory.getFileFromBase64Path(albumProject.getAlbumArtBase64Path());
            CustomAssert.fileExists(albumArtFromProject);
        }




        if (albumArtFromProject instanceof LocalFileAdapter localFileAdapter) {
            return Traper.trap(() -> Optional.of(Artwork.createArtworkFromFile(localFileAdapter.getLocalFile())));
        }

        return Optional.empty();
    }


    private FileAdapter resolveProjectFile(UUID projectId) {
        return filesFactory.albumProjectDirectory().combine(projectId + ".json");
    }


}
