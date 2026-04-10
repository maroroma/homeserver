package maroroma.homemusicplayer.services;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.tools.StreamUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

import static maroroma.homemusicplayer.tools.StreamUtils.shuffle;

@Component
@Slf4j
public class SynchronizedPlayList {

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    private final List<TrackEntity> synchronizedAndOrderedTrackList = Collections.synchronizedList(new ArrayList<>());

    private final ApplicationEventPublisher applicationEventPublisher;

    public SynchronizedPlayList(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public List<TrackEntity> fullTrackList() {
        return Collections.unmodifiableList(synchronizedAndOrderedTrackList);
    }

    public List<TrackEntity> allTracksButCurrentTrack() {
        if (isEmpty()) {
            return Collections.emptyList();
        }

        return fullTrackList()
                .stream()
                .filter(trackEntity -> getOptionalCurrentTrack().map(TrackEntity::getId)
                        .map(currentId -> !trackEntity.getId().equals(currentId))
                        .orElse(false))
                .toList();
    }

    @Synchronized
    public SynchronizedPlayList clearAndAdd(UUID selectedFirstTrackId, List<TrackEntity> allTracks) {
        // récupération du morceau sélectionné pour la lecture + les autres
        var tracksSelectedAndNotSelected = StreamUtils.of(allTracks)
                .collect(Collectors.groupingBy(trackEntity -> trackEntity.getId().equals(selectedFirstTrackId)));

        // premier morceau
        var finalOrderedListToPlay = new ArrayList<>(tracksSelectedAndNotSelected.get(true));


        // les autres, randomizés
        var otherTracksToPlay = new ArrayList<>(StreamUtils.of(tracksSelectedAndNotSelected.get(false))
                .collect(shuffle()));

        finalOrderedListToPlay.addAll(otherTracksToPlay);

        log.info("Track selected to play : {}, other tracks : {}",
                finalOrderedListToPlay.get(0).getName(),
                otherTracksToPlay.stream().map(TrackEntity::getName).toList());

        return clearAndStop()
                .updateSynchronizedAndOrderedTrackList(trackList -> {
                    trackList.clear();
                    trackList.addAll(finalOrderedListToPlay);
                })
                .updateCurrentTrack(previousTrackIndex -> 0);
    }

    public Optional<TrackEntity> getOptionalCurrentTrack() {
        return Optional.of(this.synchronizedAndOrderedTrackList)
                .filter(Predicate.not(Collection::isEmpty))
                .map(notAnEmptyList -> notAnEmptyList.get(this.currentIndex.get()));
    }

    public SynchronizedPlayList clearAndStop() {
        updateSynchronizedAndOrderedTrackList(synchronizedAndOrderedTrackList::removeAll);
        this.applicationEventPublisher.publishEvent(new StoppedPlayListEvent());
        return this;
    }

    public SynchronizedPlayList next() {
        return updateCurrentTrack(previousTrackIndex -> {
            if (previousTrackIndex + 1 < this.synchronizedAndOrderedTrackList.size()) {
                return previousTrackIndex + 1;
            } else {
                return 0;
            }
        });
    }

    public SynchronizedPlayList previous() {
        return updateCurrentTrack(previousTrackIndex -> {
            if (previousTrackIndex - 1 >= 0) {
                return previousTrackIndex - 1;
            } else {
                return this.synchronizedAndOrderedTrackList.size() - 1;
            }
        });
    }

    public boolean isEmpty() {
        return this.synchronizedAndOrderedTrackList.isEmpty();
    }

    @Synchronized
    public SynchronizedPlayList addAll(List<TrackEntity> tracksToAdd) {

        if (this.isEmpty()) {
            return clearAndAdd(tracksToAdd.get(0).getId(), tracksToAdd);
        } else {
            return this.updateSynchronizedAndOrderedTrackList(trackList ->
                    {
                        var newMergedAndRandomizedTrackList = Stream.concat(
                                        StreamUtils.of(trackList),
                                        StreamUtils.of(tracksToAdd))
                                .collect(shuffle());

                        var currentIndexInNewPlayList = newMergedAndRandomizedTrackList.indexOf(getOptionalCurrentTrack().orElseThrow());
                        trackList.clear();
                        trackList.addAll(newMergedAndRandomizedTrackList);
                        log.info("playlist updated with {} new tracks", tracksToAdd.size());
                        log.info("applying current index from {} to {}", this.currentIndex.get(), currentIndexInNewPlayList);
                        this.currentIndex.set(currentIndexInNewPlayList);
                    }
            );
        }
    }

    private SynchronizedPlayList updateSynchronizedAndOrderedTrackList(Consumer<List<TrackEntity>> actionsOnTracks) {
        actionsOnTracks.accept(synchronizedAndOrderedTrackList);
        this.applicationEventPublisher.publishEvent(new UpdatedPlayListContentEvent(this));
        return this;
    }

    private SynchronizedPlayList updateCurrentTrack(IntUnaryOperator newCurrentTrackIndexSupplier) {
        this.currentIndex.set(newCurrentTrackIndexSupplier.applyAsInt(this.currentIndex.get()));
        this.applicationEventPublisher.publishEvent(new UpdatedCurrentTrackEvent());
        return this;
    }

    public record UpdatedPlayListContentEvent(SynchronizedPlayList updatedPlayList) {
    }

    public record StoppedPlayListEvent() {

    }

    public record UpdatedCurrentTrackEvent() {
    }
}
