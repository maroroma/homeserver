package maroroma.homemusicplayer.services.mp3;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.tools.MusicPlayerException;

import java.io.*;
import java.util.*;
import java.util.function.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

/**
 * Dérivé de thread qui réalise la lecture d'un mp3
 * La classe a une durée de vie qui est celle du mp3 qui est lu
 */
@RequiredArgsConstructor
public class Mp3Task extends Thread {

    private final InputStream inputStream;

    private final Mp3Player mp3Player;
    private AudioInputStream audioInputStream;

    private SourceDataLine sourceDataLine;

    /**
     * En passant à true, ce boolean indique que la lecture doit prendre fin
     * il permet de sortir de la boucle de parcourt des streams
     */
    private boolean stopRequired = false;


    private Mp3TaskEventListener mp3TaskStoppedEventListener;
    private Mp3TaskEventListener mp3TaskEndedEventListener;

    public Optional<NaturalVolumeControl> getVolumeControl() {
        return NaturalVolumeControl.from(this.sourceDataLine);
    }

    @Override
    public void run() {

        boolean hasForciblyStopped = false;


        try {
            audioInputStream = getAudioInputStream(this.inputStream);
            final AudioFormat outFormat = getOutFormat(audioInputStream.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            if (sourceDataLine != null) {
                sourceDataLine.open(outFormat);
                this.mp3Player.getLastKnownVolume()
                        .ifPresent(knownVolume ->  this.getVolumeControl().ifPresent(control -> control.setCurrentVolume(knownVolume)));
                sourceDataLine.start();
                hasForciblyStopped = stream(getAudioInputStream(outFormat, audioInputStream), sourceDataLine);
                sourceDataLine.drain();
                sourceDataLine.stop();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new MusicPlayerException("exception while playing a track", e);
        } finally {

            if (this.sourceDataLine != null) {
                this.sourceDataLine.close();
            }

            if (this.audioInputStream != null) {
                try {
                    this.audioInputStream.close();
                } catch (IOException e) {
                    throw new MusicPlayerException("exception while closing a track", e);
                }
            }


            // gestion des events relatifs à la fin de la lecture (ça permet de remonter
            // des modifications de la playlist)
            if (hasForciblyStopped) {
                Optional.ofNullable(this.mp3TaskStoppedEventListener)
                        .ifPresent(listener -> listener.accept(this));
            } else {
                Optional.ofNullable(this.mp3TaskEndedEventListener)
                        .ifPresent(listener -> listener.accept(this));
            }


        }

    }

    public Mp3Task addStoppedEventListener(Mp3TaskEventListener mp3TaskEventListener) {
        this.mp3TaskStoppedEventListener = mp3TaskEventListener;
        return this;
    }

    public Mp3Task addEndedEventListener(Mp3TaskEventListener mp3TaskEndedEventListener) {
        this.mp3TaskEndedEventListener = mp3TaskEndedEventListener;
        return this;
    }

    public interface Mp3TaskEventListener extends Consumer<Mp3Task> {

    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private boolean stream(AudioInputStream audioInputStream, SourceDataLine sourceDataLineAsOutput)
            throws IOException {
        final byte[] buffer = new byte[4096 * 2];

        // la boucle peut être arrêtée si un stop est requis
        for (int n = 0; n != -1 && !this.stopRequired; ) {
            // on avance plus les index si une pause est positionnée (on rend la boucle un peut bourrin mais bon)
            if (!this.mp3Player.isPaused()) {
                n = audioInputStream.read(buffer, 0, buffer.length);
                if (n != -1) {
                    sourceDataLineAsOutput.write(buffer, 0, n);
                }
            }
        }
        return this.stopRequired;
    }


    public void stopRequested() {
        this.stopRequired = true;
    }

    public Mp3Task startThen() {
        this.start();
        return this;
    }
}
