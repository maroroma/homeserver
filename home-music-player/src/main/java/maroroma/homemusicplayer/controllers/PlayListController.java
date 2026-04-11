package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.PlayList;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.services.PlayListService;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListService playListService;
    private final TrackMapper trackMapper;

    @PostMapping("api/musicplayer/playlists")
    public ResponseEntity<PlayList> createPlayList(@RequestBody PlayList playList) {
        return ResponseEntity.ok(playListService.createPlayList(playList));
    }

    @PatchMapping("api/musicplayer/playlists/{playListId}/name/{newName}")
    public ResponseEntity<PlayList> renamePlayList(@PathVariable("playListId") UUID playListId, @PathVariable("newName") String newName) {
        return ResponseEntity.ok(playListService.renamePlayList(playListId, newName));
    }

    @GetMapping("api/musicplayer/playlists")
    public ResponseEntity<List<PlayList>> getAllPlayLists() {
        return ResponseEntity.ok(playListService.getAllPlayLists());
    }

    @GetMapping("api/musicplayer/playlists/{playListId}")
    public ResponseEntity<PlayList> getPlayList(@PathVariable("playListId") UUID playListId) {
        return ResponseEntity.ok(playListService.getPlayList(playListId));
    }

    @PutMapping("api/musicplayer/playlists/{playListId}/tracks/{trackToAddId}")
    public ResponseEntity<PlayList> addTrackToPlayList(@PathVariable("playListId") UUID playListId,
                                                       @PathVariable("trackToAddId") UUID trackToAddId) {
        return ResponseEntity.ok(playListService.addTrackToPlayList(playListId, trackToAddId));
    }

    @DeleteMapping("api/musicplayer/playlists/{playListId}/tracks/{trackToAddId}")
    public ResponseEntity<PlayList> deleteTrackFromPlayList(@PathVariable("playListId") UUID playListId,
                                                            @PathVariable("trackToAddId") UUID trackToAddId) {
        return ResponseEntity.ok(playListService.removeTrackFromPlayList(playListId, trackToAddId));
    }

    @DeleteMapping("api/musicplayer/playlists/{playListId}")
    public ResponseEntity<Boolean> deleteTrackFromPlayList(@PathVariable("playListId") UUID playListId) {
        return ResponseEntity.ok(playListService.deletePlayList(playListId));
    }

    @GetMapping("api/musicplayer/playlists/{playListId}/tracks")
    public ResponseEntity<List<Track>> getTracksFromPlayList(@PathVariable("playListId") UUID playListId) {
        return ResponseEntity.ok(trackMapper.mapToModel(playListService.getTracksFromPlayList(playListId)));
    }
}
