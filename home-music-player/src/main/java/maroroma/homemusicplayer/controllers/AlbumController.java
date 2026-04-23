package maroroma.homemusicplayer.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Album;
import maroroma.homemusicplayer.model.library.api.AlbumProject;
import maroroma.homemusicplayer.model.library.api.CreateAlbumProjectRequest;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.services.AlbumProjectService;
import maroroma.homemusicplayer.services.AlbumService;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper albumMapper;
    private final AlbumProjectService albumProjectService;

    private final TrackMapper trackMapper;

    @GetMapping("api/musicplayer/library/albums")
    ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(this.albumMapper.mapToModel(albumService.getAllAlbums()));
    }

    @GetMapping("api/musicplayer/library/albums/{albumId}")
    ResponseEntity<Album> getOneAlbum(@PathVariable("albumId") UUID albumId) {
        return this.albumService.getAlbum(albumId)
                .map(albumMapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @GetMapping("api/musicplayer/library/albums/{albumId}/tracks")
    ResponseEntity<List<Track>> getTracksFromAlbum(@PathVariable("albumId") UUID albumId) {
        return ResponseEntity.ok(this.trackMapper.mapToModel(this.albumService.findTracksForAlbum(albumId)));
    }

    @PostMapping("api/musicplayer/library/albums/{albumId}/tracks")
    ResponseEntity<List<Track>> addTrackToAlbum(@PathVariable("albumId") UUID albumId, HttpServletRequest request) {
        return ResponseEntity.ok(this.trackMapper.mapToModel(this.albumService.addNewFilesToAlbum(albumId, request)));
    }

    @PostMapping("api/musicplayer/library/albums/projects")
    ResponseEntity<AlbumProject> createAlbumProject(@RequestBody CreateAlbumProjectRequest createAlbumProjectRequest) {
        return ResponseEntity.ok(this.albumProjectService.startAlbumProject(createAlbumProjectRequest));
    }

    @GetMapping("api/musicplayer/library/albums/projects/{projectId}")
    ResponseEntity<AlbumProject> createAlbumProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(this.albumProjectService.getAlbumProject(projectId));
    }

}
