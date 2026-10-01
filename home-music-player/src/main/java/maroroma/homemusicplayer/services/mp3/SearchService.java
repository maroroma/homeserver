package maroroma.homemusicplayer.services.mp3;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.SearchRequest;
import maroroma.homemusicplayer.model.library.api.SearchResponse;
import maroroma.homemusicplayer.model.library.api.Track;
import maroroma.homemusicplayer.repositories.AlbumRepository;
import maroroma.homemusicplayer.repositories.ArtistRepository;
import maroroma.homemusicplayer.repositories.TrackRepository;
import maroroma.homemusicplayer.services.mappers.entities.AlbumMapper;
import maroroma.homemusicplayer.services.mappers.entities.ArtistMapper;
import maroroma.homemusicplayer.services.mappers.entities.TrackMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final TrackMapper trackMapper;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;


    public SearchResponse find(SearchRequest searchRequest) {
        if (StringUtils.isEmpty(searchRequest.getSearch())) {
            return SearchResponse.empty();
        }

        if (searchRequest.getSearch().length() < 3) {
            return SearchResponse.empty();
        }

        var tracks = trackRepository.findByNameContainsIgnoreCase(searchRequest.getSearch());
        var artists = artistRepository.findByNameContainsIgnoreCase(searchRequest.getSearch());
        var albums = albumRepository.findByNameContainsIgnoreCase(searchRequest.getSearch());

        return SearchResponse.builder()
                .tracks(tracks.stream().map(trackMapper::mapToModel).map(Track::lightWeight).toList())
                .artists(artists.stream().map(artistMapper::lazyMapToModel).toList())
                .albums(albums.stream().map(albumMapper::lazyMapToModel).toList())
                .build();
    }

}
