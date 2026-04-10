package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.services.SimplePlayerService;
import maroroma.homemusicplayer.services.mp3.InputStreamManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CacheController {

    private final InputStreamManager inputStreamManager;
    private final SimplePlayerService simplePlayerService;


    @DeleteMapping("api/musicplayer/cache")
    public ResponseEntity<Object> clearCache() {
        // comme ça on peut cleaner sans pb de lecture/ecriture
        simplePlayerService.stop();
        inputStreamManager.clearAllLocalFileCache();
        return ResponseEntity.noContent().build();
    }
}
