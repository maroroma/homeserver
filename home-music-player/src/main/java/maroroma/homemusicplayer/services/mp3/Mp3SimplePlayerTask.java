package maroroma.homemusicplayer.services.mp3;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maroroma.homemusicplayer.services.SynchronizedPlayList;
import maroroma.homemusicplayer.tools.MusicPlayerException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class Mp3SimplePlayerTask extends Thread {

    public static final int DEFAULT_INITIAL_VOLUME = 50;
    private final SynchronizedPlayList synchronizedPlayList;
    private final InputStreamManager inputStreamManager;
    private final AtomicBoolean forceStopRequired = new AtomicBoolean(false);
    private final AtomicBoolean changeTrackRequired = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final AtomicInteger lastKnownVolume = new AtomicInteger(DEFAULT_INITIAL_VOLUME);

    private SourceDataLine sourceDataLine;

    private Optional<NaturalVolumeControl> getVolumeControl() {
        return NaturalVolumeControl.from(this.sourceDataLine, lastKnownVolume);
    }


    @Override
    public void run() {
        log.info("Starting Mp3SimplePlayerTask");
        while (true) {
            this.inputStreamManager
                    .resolveCurrentInputStream()
                    .ifPresent(this::playCurrentTrack);
        }
    }


    private void playCurrentTrack(InputStreamManager.TrackWithInputStream trackWithInputStream) {
        AudioInputStream audioInputStream = null;
        log.info("Start Playing track : <{}>", trackWithInputStream.trackEntity().getName());
        this.forceStopRequired.set(false);
        this.changeTrackRequired.set(false);

        try {
            audioInputStream = getAudioInputStream(trackWithInputStream.inputStream());
            final AudioFormat outFormat = getOutFormat(audioInputStream.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            if (sourceDataLine != null) {
                sourceDataLine.open(outFormat);

                // réapplication du dernier volume connu avant le rédémarrage de la piste
                // ici peut être un bug, on constate des musiques trop fortes lors du parcours de la playlist....
                this.getVolumeControl().ifPresent(NaturalVolumeControl::updateInnerVolumeValue);

                sourceDataLine.start();
                stream(getAudioInputStream(outFormat, audioInputStream), sourceDataLine);
                sourceDataLine.drain();
                sourceDataLine.stop();
            }

            switch (EndTrackReason.from(this)) {
                case FORCE_STOP -> log.info("Track {} stopped by user", trackWithInputStream.trackEntity().getName());
                case CHANGE_TRACK -> log.info("Track {} stopped to change track by user", trackWithInputStream.trackEntity().getName());
                case END_OF_STREAM -> {
                    log.info("Track {} ended normally (end of stream reached)", trackWithInputStream.trackEntity().getName());
                    this.synchronizedPlayList.next();
                }
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.error("Error while playing track {}. Reinit all status to false.", trackWithInputStream.trackEntity().getName(), e);
            this.forceStopRequired.set(false);
            this.changeTrackRequired.set(false);
            this.paused.set(false);
            throw new MusicPlayerException("exception while playing a track", e);
        } finally {
            Optional.ofNullable(sourceDataLine)
                    .ifPresent(SourceDataLine::close);

            if (audioInputStream != null) {
                try {
                    audioInputStream.close();
                } catch (IOException e) {
                    throw new MusicPlayerException("exception while closing a track", e);
                }
            }
        }
    }

    private void stream(AudioInputStream audioInputStream, SourceDataLine sourceDataLineAsOutput)
            throws IOException {
        final byte[] buffer = new byte[4096 * 2];

        log.info("Start streaming track");

        // la boucle peut être arrêtée si un stop est requis ou si le stream est terminé
        for (int n = 0; n != -1 && !this.forceStopRequired.get() && !this.changeTrackRequired.get(); ) {
            // on avance plus les index si une pause est positionnée (on rend la boucle un peut bourrin mais bon)
            if (!this.paused.get()) {
//                log.info("read chunk of stream");
                n = audioInputStream.read(buffer, 0, buffer.length);
                if (n != -1) {
                    sourceDataLineAsOutput.write(buffer, 0, n);
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        this.start();
    }

    public void pause() {
        this.paused.set(true);
    }

    public void unpause() {
        this.paused.set(false);
    }

    public boolean isPaused() {return this.paused.get();}

    public void volumeUp() {
        this.getVolumeControl().map(NaturalVolumeControl::volumeUp).ifPresent(this.lastKnownVolume::set);
    }

    public void volumeDown() {
        this.getVolumeControl().map(NaturalVolumeControl::volumeDown).ifPresent(this.lastKnownVolume::set);
    }

    public int volumeValue() {
        return this.getVolumeControl().map(NaturalVolumeControl::getCurrentVolume).orElseGet(this.lastKnownVolume::get);
    }

    @EventListener
    @Async
    public void updatedTrackEventListener(SynchronizedPlayList.UpdatedCurrentTrackEvent event) {
        this.changeTrackRequired.set(true);
    }

    @EventListener
    @Async
    public void stopPlayListEventListener(final SynchronizedPlayList.StoppedPlayListEvent event) {
        this.forceStopRequired.set(true);
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private enum EndTrackReason {
        FORCE_STOP,
        CHANGE_TRACK,
        END_OF_STREAM;

        static EndTrackReason from(Mp3SimplePlayerTask task) {
            if (task.forceStopRequired.get()) {
                return EndTrackReason.FORCE_STOP;
            }

            if (task.changeTrackRequired.get()) {
                return EndTrackReason.CHANGE_TRACK;
            }

            return END_OF_STREAM;
        }
    }
}
