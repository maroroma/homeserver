package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Artist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController()
@RequiredArgsConstructor
public class TrackController {

    @GetMapping("musicplayer/library/tracks")
    ResponseEntity<List<Artist>> getAllTracks() {
        return null;
    }
    @GetMapping("musicplayer/library/tracks/{trackId}")
    ResponseEntity<Artist> getOneArtist(@PathVariable("trackId") UUID trackId) {
        return null;
    }


}
