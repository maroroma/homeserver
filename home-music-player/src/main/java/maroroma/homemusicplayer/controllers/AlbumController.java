package maroroma.homemusicplayer.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.*;
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

    @PostMapping("api/musicplayer/library/albums/projects/{projectId}/tracks")
    ResponseEntity<AlbumProject> addTrackToProject(@PathVariable("projectId") UUID projectId,  HttpServletRequest request) {
        return ResponseEntity.ok(this.albumProjectService.addNewFilesToProject(projectId, request));
    }

    @PatchMapping("api/musicplayer/library/albums/projects/{projectId}/musicsource")
    ResponseEntity<AlbumProject> createAlbumOnMusicSource(@PathVariable("projectId") UUID projectId) {
        return ResponseEntity.ok(this.albumProjectService.createAlbumOnMusicSource(projectId));
    }

    @PatchMapping("api/musicplayer/library/albums/projects/{projectId}/musicsource/tracks")
    ResponseEntity<AlbumProject> copyFileToMusicSource(@PathVariable("projectId") UUID projectId, @RequestBody TargetedFileByName targetedFileByName) {
        return ResponseEntity.ok(this.albumProjectService.copyTrackToMusicSource(projectId, targetedFileByName));
    }

    @PatchMapping("api/musicplayer/library/albums/projects/{projectId}/tracks/name")
    ResponseEntity<AlbumProject> renameOneTrack(@PathVariable("projectId") UUID projectId, @RequestBody RenameOneFileRequest renameOneFileRequest) {
        return ResponseEntity.ok(this.albumProjectService.renameOneFile(projectId, renameOneFileRequest));
    }

    @PatchMapping("api/musicplayer/library/albums/projects/{projectId}/tracks/tags")
    ResponseEntity<AlbumProject> renameOneTrack(@PathVariable("projectId") UUID projectId, @RequestBody TargetedFileByName targetedFileByName) {
        return ResponseEntity.ok(this.albumProjectService.applyMp3Tags(projectId, targetedFileByName));
    }

    @GetMapping("api/musicplayer/library/albums/projects/{projectId}")
    ResponseEntity<AlbumProject> getAlbumProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(this.albumProjectService.getAlbumProject(projectId));
    }

    @DeleteMapping("api/musicplayer/library/albums/projects/{projectId}")
    ResponseEntity<AlbumProject> deleteAlbumProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(this.albumProjectService.deleteAlbumProject(projectId));
    }

}
