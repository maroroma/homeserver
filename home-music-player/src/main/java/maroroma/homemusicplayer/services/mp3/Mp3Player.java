package maroroma.homemusicplayer.services.mp3;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.library.entities.TrackEntity;
import maroroma.homemusicplayer.model.player.api.PlayerStatus;
import maroroma.homemusicplayer.services.FilesFactory;
import maroroma.homemusicplayer.services.InputStreamCache;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
public class Mp3Player {


    private final FilesFactory filesFactory;

    private final InputStreamCache inputStreamCache;

    Queue<Mp3Task> mp3Tasks = new LinkedBlockingQueue<>();

    public void stop() {
        this.getCurrentTask().ifPresent(currentTask -> {
            currentTask.addEndedEventListener(null)
                    .addStoppedEventListener(stoppedTask -> mp3Tasks.poll())
                    .stopRequested();
        });
    }

    public void pause() {
        this.getCurrentTask().ifPresent(Mp3Task::pause);
    }

    public void resume() {
        this.getCurrentTask().ifPresent(Mp3Task::unpause);
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
                    .addEndedEventListener(null)
                    // on prépare la requete async d'arret en changeant l'event listener
                    .addStoppedEventListener(stoppedTrack -> {
                        // retrait de l'item
                        this.mp3Tasks.poll();
                        // ajout du nouveau, pour lequel on vient de demander l'arret
                        this.mp3Tasks.add(
                                new Mp3Task(this.inputStreamCache.getInputStream(currentTrack))
//                                new Mp3Task(this.filesFactory.getFileFromBase64Path(currentTrack.getLibraryItemPath()))
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
            // si pas de lecture en cours création standard
            this.mp3Tasks.add(new Mp3Task(this.inputStreamCache.getInputStream(currentTrack))
//            this.mp3Tasks.add(new Mp3Task(this.filesFactory.getFileFromBase64Path(currentTrack.getLibraryItemPath()))
                    .addEndedEventListener(endedTask -> {
                        this.mp3Tasks.poll();
                        endedEventListener.accept(endedTask);
                    })
                    .startThen());
        });


    }

    public NaturalVolumeControl getVolumeControl() {
        return this.getCurrentTask().map(Mp3Task::getVolumeControl).orElseGet(NaturalVolumeControl.NoopNaturalVolumeControl::new);
    }

    private Optional<Mp3Task> getCurrentTask() {
        return Optional.ofNullable(this.mp3Tasks.peek());
    }

    public PlayerStatus getPlayerStatus() {
        return this.getCurrentTask()
                .map(Mp3Task::getPlayerStatus)
                .orElse(PlayerStatus.STOPPED);
    }

}
