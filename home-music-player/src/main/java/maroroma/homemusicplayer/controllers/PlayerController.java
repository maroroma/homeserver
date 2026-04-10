package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.model.player.api.AddAlbumToPlayListRequest;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;
import maroroma.homemusicplayer.model.player.api.FullPlayerStatus;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.PlayerStatusGenerator;
import maroroma.homemusicplayer.services.SimplePlayerService;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final SimplePlayerService simplePlayerService;
    private final PlayerStatusGenerator playerStatusGenerator;
    private final TrackMapper trackMapper;

    @PostMapping("api/musicplayer/player")
    public ResponseEntity<Boolean> playFromAlbum(@RequestBody CreatePlayerRequest createPlayerRequest) {
        this.simplePlayerService.play(createPlayerRequest);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("api/musicplayer/player")
    public ResponseEntity<Boolean> delete() {
        this.simplePlayerService.stop();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/musicplayer/player/status/pause")
    public ResponseEntity<Boolean> pause() {
        this.simplePlayerService.pause();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/musicplayer/player/status/resume")
    public ResponseEntity<Boolean> resume() {
        this.simplePlayerService.resume();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/musicplayer/player/playlist/next")
    public ResponseEntity<Boolean> next() {
        this.simplePlayerService.next();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/musicplayer/player/playlist/previous")
    public ResponseEntity<Boolean> previous() {
        this.simplePlayerService.previous();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/musicplayer/player/volume/up")
    public ResponseEntity<Boolean> volumeUp() {
        this.simplePlayerService.volumeUp();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("api/musicplayer/player/volume/down")
    public ResponseEntity<Boolean> volumeDown() {
        this.simplePlayerService.volumeDown();
        return ResponseEntity.ok(true);
    }

    @PutMapping("api/musicplayer/player/playlist")
    public ResponseEntity<Boolean> addAlbum(@RequestBody AddAlbumToPlayListRequest addAlbumToPlayListRequest) {
        this.simplePlayerService.addAlbumToPlayList(addAlbumToPlayListRequest);
        return ResponseEntity.ok(true);
    }

    @GetMapping("api/musicplayer/player/status")
    public ResponseEntity<PlayerStatus> getPlayerStatus() {
        return ResponseEntity.ok(playerStatusGenerator.resolvePlayerStatus());
    }

    @GetMapping("api/musicplayer/player/status/full")
    public ResponseEntity<FullPlayerStatus> getFullPlayerStatus() {
        return ResponseEntity.ok(playerStatusGenerator.generatePlayerStatus());
    }

    @GetMapping("api/musicplayer/player/playlist")
    public ResponseEntity<List<Track>> getPlayList() {
        return ResponseEntity.ok(trackMapper.mapToModel(simplePlayerService.getPlayListTracks()));
    }

}
