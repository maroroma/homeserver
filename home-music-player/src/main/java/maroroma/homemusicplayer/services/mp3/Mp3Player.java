package maroroma.homemusicplayer.services.mp3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.caches.InputStreamCache;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class Mp3Player {


    private final FilesFactory filesFactory;

    private final InputStreamCache inputStreamCache;

    private AtomicBoolean isPaused = new AtomicBoolean(false);
    private AtomicBoolean isLoading = new AtomicBoolean(false);

    private int lastKnownVolume = 50;

    Queue<Mp3Task> mp3Tasks = new LinkedBlockingQueue<>();

    public void stop() {
        this.getCurrentTask().ifPresent(currentTask -> {
            currentTask.addEndedEventListener(null)
                    .addStoppedEventListener(stoppedTask -> mp3Tasks.poll())
                    .stopRequested();
        });
    }

    public boolean isPaused() {
        return this.isPaused.get();
    }

    public void pause() {
        this.isPaused.set(true);
    }

    public void resume() {
        this.isPaused.set(false);
    }

    public void play(TrackEntity currentTrack,
                     Mp3Task.Mp3TaskEventListener endedEventListener) {


        // si le fichier n'est pas accessible (erreur ponctuelle ?)
        if (!this.filesFactory.getFileFromBase64Path(currentTrack.getLibraryItemPath()).exists()) {
            endedEventListener.accept(null);
            return;
        }

        this.getCurrentTask().ifPresentOrElse(currentTask -> {
            // si lecture déjà en cours, on vire l'event de next, vu que l'on remplace le track
            currentTask
                    .removeEndedEventListerner()
                    // on prépare la requete async d'arret en changeant l'event listener
                    .addStoppedEventListener(stoppedTrack -> {
                        // retrait de l'item
                        this.mp3Tasks.poll();
                        // ajout du nouveau, pour lequel on vient de demander l'arret
                        this.mp3Tasks.add(
                                new Mp3Task(loadInputStream(currentTrack), this)
                                        .addEndedEventListener(endedTask -> {
                                            this.mp3Tasks.poll();
                                            endedEventListener.accept(endedTask);
                                        })
                                        .startThen());
                    })
                    // demande d'arret du morceau en cours (propre), pour laisser le prochain morceau démarré
                    .stopRequested();

        }, () ->

        {
            // si pas de lecture en cours, création standard
            this.mp3Tasks.add(new Mp3Task(loadInputStream(currentTrack), this)
                    .addEndedEventListener(endedTask -> {
                        this.mp3Tasks.poll();
                        endedEventListener.accept(endedTask);
                    })
                    .startThen());
        });


    }

    private NaturalVolumeControl getVolumeControl() {
        return this.getCurrentTask()
                .flatMap(Mp3Task::getVolumeControl)
                .orElseGet(() -> new NaturalVolumeControl.NoopNaturalVolumeControl(this.getLastKnownVolume()));
    }

    public int getVolume() {
        return this.getVolumeControl().getCurrentVolume();
    }

    public void setLastKnownVolume(int volume) {
//        log.info("new volume:" + volume);
        this.lastKnownVolume = volume;
    }

    public void doWithVolumeControl(Function<NaturalVolumeControl, Integer> naturalVolumeControlConsumer) {
        setLastKnownVolume(naturalVolumeControlConsumer.apply(getVolumeControl()));
    }

    public Integer getLastKnownVolume() {
        return this.lastKnownVolume;
    }


    public PlayerStatus getPlayerStatus() {

        if (this.isLoading.get()) {
            return PlayerStatus.LOADING;
        }

        return this.getCurrentTask()
                .map(taks -> this.isPaused() ? PlayerStatus.PAUSED : PlayerStatus.PLAYING)
                .orElse(PlayerStatus.STOPPED);
    }


    private Optional<Mp3Task> getCurrentTask() {
        return Optional.ofNullable(this.mp3Tasks.peek());
    }

    private InputStream loadInputStream(TrackEntity trackEntity) {
        this.isLoading.set(true);
        var loadedStream = this.inputStreamCache.getInputStream(trackEntity);
        this.isLoading.set(false);
        return loadedStream;
    }
}
