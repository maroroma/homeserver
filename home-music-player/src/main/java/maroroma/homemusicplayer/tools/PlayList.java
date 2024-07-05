package maroroma.homemusicplayer.tools;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static maroroma.homemusicplayer.tools.StreamUtils.shuffle;

@RequiredArgsConstructor
@Builder(toBuilder = true)
public class PlayList {

    private final int currentIndex;
    private final List<TrackEntity> trackEntityList;

    public static PlayList empty() {
        return new PlayList(List.of());
    }

    public static PlayList of(UUID selectedFirstTrackId, List<TrackEntity> allTracks) {
        var tracksSelectedAndNotSelected = StreamUtils.of(allTracks)
                .collect(Collectors.groupingBy(trackEntity -> trackEntity.getId().equals(selectedFirstTrackId)));

        var finalOrderedListToPlay = new ArrayList<>(tracksSelectedAndNotSelected.get(true));

        var otherTracksToPlay = new ArrayList<>(StreamUtils.of(tracksSelectedAndNotSelected.get(false))
                .collect(shuffle()));

        finalOrderedListToPlay.addAll(otherTracksToPlay);

        return new PlayList(finalOrderedListToPlay);
    }

    PlayList(List<TrackEntity> trackEntityList) {
        this(0, trackEntityList);
    }

    public TrackEntity getCurrentTrack() {
        return this.trackEntityList.get(this.currentIndex);
    }

    public Optional<TrackEntity> getOptionalCurrentTrack() {
        return Optional.of(this.trackEntityList)
                .filter(Predicate.not(Collection::isEmpty))
                .map(notAnEmptyList -> notAnEmptyList.get(this.currentIndex));
    }

    public PlayList next() {
        if (this.currentIndex + 1 < this.trackEntityList.size()) {
            return toBuilder().currentIndex(this.currentIndex + 1).build();
        } else {
            return toBuilder().currentIndex(0).build();
        }
    }

    public List<TrackEntity> teaseNextTracks(int nextTracks) {
        return this.trackEntityList.subList(this.currentIndex, Math.min(this.currentIndex + nextTracks, this.trackEntityList.size() -1));
    }

    public PlayList previous() {
        if (this.currentIndex - 1 >= 0) {
            return toBuilder().currentIndex(this.currentIndex - 1).build();
        } else {
            return toBuilder().currentIndex(this.trackEntityList.size() - 1).build();
        }
    }

    public boolean isEmpty() {
        return this.trackEntityList.isEmpty();
    }

    public PlayList addAll(List<TrackEntity> tracksToAdd) {

        if (this.isEmpty()) {
            return new PlayList(tracksToAdd.stream().collect(shuffle()));
        }


        var nextList = new ArrayList<>(this.trackEntityList);
        nextList.addAll(tracksToAdd);

        return of(this.getCurrentTrack().getId(), nextList);
    }



}
