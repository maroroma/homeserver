package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.player.api.AddAlbumToPlayListRequest;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;
import maroroma.homemusicplayer.services.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlayerController {


    private final PlayerService playerService;


    @PostMapping("musicplayer/player")
    public ResponseEntity<Boolean> playFromAlbum(@RequestBody CreatePlayerRequest createPlayerRequest) {
        this.playerService.play(createPlayerRequest);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("musicplayer/player")
    public ResponseEntity<Boolean> delete() {
        this.playerService.stop();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("musicplayer/player/status/pause")
    public ResponseEntity<Boolean> pause() {
        this.playerService.pause();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("musicplayer/player/status/resume")
    public ResponseEntity<Boolean> resume() {
        this.playerService.resume();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("musicplayer/player/playlist/next")
    public ResponseEntity<Boolean> next() {
        this.playerService.next();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("musicplayer/player/playlist/previous")
    public ResponseEntity<Boolean> previous() {
        this.playerService.previous();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("musicplayer/player/volume/up")
    public ResponseEntity<Boolean> volumeUp() {
        this.playerService.volumeUp();
        return ResponseEntity.ok(true);
    }

    @PatchMapping("musicplayer/player/volume/down")
    public ResponseEntity<Boolean> volumeDown() {
        this.playerService.volumeDown();
        return ResponseEntity.ok(true);
    }

    @PutMapping("musicplayer/player/playlist")
    public ResponseEntity<Boolean> addAlbum(@RequestBody AddAlbumToPlayListRequest addAlbumToPlayListRequest) {
        this.playerService.addAlbumToPlayList(addAlbumToPlayListRequest);
        return ResponseEntity.ok(true);
    }



}
