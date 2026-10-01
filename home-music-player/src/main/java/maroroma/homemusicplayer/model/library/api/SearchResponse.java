package maroroma.homemusicplayer.model.library.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private List<Track> tracks;
    private List<Album> albums;
    private List<Artist> artists;

    public static SearchResponse empty()  {
        return SearchResponse.builder()
                .tracks(List.of())
                .albums(List.of())
                .artists(List.of())
                .build();
    }
}
