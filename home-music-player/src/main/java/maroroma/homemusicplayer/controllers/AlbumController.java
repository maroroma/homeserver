package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Album;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.services.AlbumService;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumMapper albumMapper;

    private final TrackMapper trackMapper;

    @GetMapping("musicplayer/library/albums")
    ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(this.albumMapper.mapToModel(albumService.getAllAlbums()));
    }

    @GetMapping("musicplayer/library/albums/{albumId}")
    ResponseEntity<Album> getOneAlbum(@PathVariable("albumId") UUID albumId) {
        return this.albumService.getAlbum(albumId)
                .map(albumMapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @GetMapping("musicplayer/library/albums/{albumId}/tracks")
    ResponseEntity<List<Track>> getTracksFromAlbum(@PathVariable("albumId") UUID albumId) {
        return ResponseEntity.ok(this.trackMapper.mapToModel(this.albumService.findTracksForAlbum(albumId)));
    }

}
