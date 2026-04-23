package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.FileAdapter;
import maroroma.homemusicplayer.model.library.api.AlbumProject;
import maroroma.homemusicplayer.model.library.api.CreateAlbumProjectRequest;
import maroroma.homemusicplayer.tools.CustomAssert;
import maroroma.homemusicplayer.tools.CustomObjectMapper;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumProjectService {
    private final ArtistService artistService;
    private final FilesFactory filesFactory;
    private final UploadResourcesService uploadResourcesService;
    private final CustomObjectMapper customObjectMapper;

    public AlbumProject getAlbumProject(UUID albumProjectId) {

        var projectFile = resolveProjectFile(albumProjectId);
        CustomAssert.fileExists(projectFile);

        return customObjectMapper.read(AlbumProject.class, projectFile);
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
                .albumName(createAlbumProjectRequest.getAlbumName())
                .projectPath(projectDirectory.pathAsBase64())
                .albumArtBase64Path(albumArt.pathAsBase64())
                .build();

        customObjectMapper.save(projectToSave, resolveProjectFile(projectID));

        return projectToSave;
    }

    private FileAdapter resolveProjectFile(UUID projectId) {
        return filesFactory.albumProjectDirectory().combine(projectId + ".json");
    }

}
