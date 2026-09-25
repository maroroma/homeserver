package maroroma.homemusicplayer.model.embedded.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.api.Track;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TrackList {
    private List<Track> tracks;
}
