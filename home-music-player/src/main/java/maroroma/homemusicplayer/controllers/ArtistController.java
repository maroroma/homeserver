package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.files.api.SimpleFile;
import maroroma.homemusicplayer.model.library.api.AddAlbumToArtistRequest;
import maroroma.homemusicplayer.model.library.api.Album;
import maroroma.homemusicplayer.model.library.api.Artist;
import maroroma.homemusicplayer.model.library.api.CreateArtistRequest;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.model.library.api.UpdateArtistRequest;
import maroroma.homemusicplayer.services.ArtistService;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import maroroma.homemusicplayer.services.mappers.files.FileAdapterMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController()
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    private final AlbumMapper albumMapper;

    private final TrackMapper trackMapper;

    private final FileAdapterMapper fileAdapterMapper;

    @GetMapping("musicplayer/library/artists")
    ResponseEntity<List<Artist>> getAllArtists() {
        return ResponseEntity.ok(this.artistMapper.mapToModel(artistService.getAllArtists()));
    }

    @GetMapping("musicplayer/library/artists/{artistId}")
    ResponseEntity<Artist> getOneArtist(@PathVariable("artistId") UUID artistId) {
        return this.artistService.getArtist(artistId)
                .map(artistMapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    @GetMapping("musicplayer/library/artists/{artistId}/tracks")
    ResponseEntity<List<Track>> getTracksFromArtist(@PathVariable("artistId") UUID artistId) {
        return ResponseEntity.ok(this.trackMapper.mapToModel(this.artistService.getTracksFromArtist(artistId)));
    }

    @GetMapping("musicplayer/library/artists/{artistId}/albums")
    ResponseEntity<List<Album>> getAlbumsFromArtist(@PathVariable("artistId") UUID artistId) {
        return ResponseEntity.ok(this.albumMapper.mapToModel(this.artistService.getArtistAlbums(artistId)));
    }

    @DeleteMapping("musicplayer/library/artists/{artistId}/albums/{albumId}")
    ResponseEntity<Artist> removeAlbumFromArtist(@PathVariable("artistId") UUID artistId, @PathVariable("albumId") UUID albumId) {
        return ResponseEntity.ok(this.artistMapper.mapToModel(this.artistService.removeAlbumFromArtist(artistId, albumId)));
    }

    @PatchMapping("musicplayer/library/artists/{artistId}/albums")
    ResponseEntity<Artist> addAlbumToArtist(@PathVariable("artistId") UUID artistId, @RequestBody AddAlbumToArtistRequest addAlbumToArtistRequest) {
        return ResponseEntity.ok(this.artistMapper.mapToModel(this.artistService.addAlbumToArtist(artistId, addAlbumToArtistRequest)));
    }

    @PatchMapping("musicplayer/library/artists/{artistId}/albums/scan")
    ResponseEntity<Artist> scanAlbumsForArtist(@PathVariable("artistId") UUID artistId) {
        return ResponseEntity.ok(this.artistMapper.mapToModel(this.artistService.scanAndAddNewAlbumsForArtist(artistId)));
    }

    @DeleteMapping("musicplayer/library/artists/{artistId}")
    ResponseEntity<List<Artist>> deleteArtist(@PathVariable("artistId") UUID artistId) {
        return ResponseEntity.ok(this.artistMapper.mapToModel(artistService.deleteArtist(artistId)));
    }

    @DeleteMapping("musicplayer/library/artists")
    ResponseEntity<List<Artist>> deleteArtist() {
        return ResponseEntity.ok(this.artistMapper.mapToModel(artistService.deleteArtists()));
    }

    @PostMapping("musicplayer/library/artists")
    ResponseEntity<List<Artist>> createArtist(@RequestBody CreateArtistRequest createArtistRequest) {
        return ResponseEntity.ok(this.artistMapper.mapToModel(artistService.addArtist(createArtistRequest)));
    }

    @PatchMapping("musicplayer/library/artists/{artistId}")
    ResponseEntity<Artist> updateArtist(@PathVariable("artistId") UUID artistId, @RequestBody UpdateArtistRequest updateArtistRequest) {
        return ResponseEntity.ok(this.artistMapper.mapToModel(artistService.updateArtist(artistId, updateArtistRequest)));
    }

    @GetMapping("musicplayer/library/artists/candidates")
    ResponseEntity<List<SimpleFile>> getArtistCandidates() {
        return ResponseEntity.ok(this.fileAdapterMapper.map(artistService.getArtistCandidates()));
    }

    @GetMapping("musicplayer/library/artists/{artistId}/albums/candidates")
    ResponseEntity<List<SimpleFile>> getAlbumCandidates(@PathVariable("artistId") UUID artistId) {
        return ResponseEntity.ok(this.fileAdapterMapper.map(artistService.getAlbumCandidates(artistId)));
    }


}
