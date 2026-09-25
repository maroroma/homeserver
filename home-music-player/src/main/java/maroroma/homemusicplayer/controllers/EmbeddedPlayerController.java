package maroroma.homemusicplayer.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.embedded.api.TrackList;
import maroroma.homemusicplayer.model.player.api.CreatePlayerRequest;
import maroroma.homemusicplayer.services.LocalResourcesService;
import maroroma.homemusicplayer.services.embedded.EmbeddedPlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmbeddedPlayerController {

    private final EmbeddedPlayerService embeddedPlayerService;
    private final LocalResourcesService localResourcesService;

    @PostMapping("api/musicplayer/embedded/tracklist")
    ResponseEntity<TrackList> generatePlayList(@RequestBody CreatePlayerRequest createPlayerRequest) {
        return ResponseEntity.ok(embeddedPlayerService.generatePlayList(createPlayerRequest));
    }

    @GetMapping("api/musicplayer/embedded/track/{trackId}/stream")
    public void getThumbByAlbum(@PathVariable("trackId") final UUID trackId, final HttpServletResponse response) {
        this.localResourcesService.streamTrack(trackId, response);
    }

}
