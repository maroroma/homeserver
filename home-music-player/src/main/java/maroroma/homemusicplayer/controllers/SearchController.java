package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.SearchRequest;
import maroroma.homemusicplayer.model.library.api.SearchResponse;
import maroroma.homemusicplayer.services.mp3.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping("api/musicplayer/search")
    public ResponseEntity<SearchResponse> clearCache(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(this.searchService.find(searchRequest));
    }
}
