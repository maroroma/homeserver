package maroroma.homemusicplayer.services.mp3;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.*;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;


/**
 * Réalise la conversion entre les valeur arbitrairement retournées par le master_gain de la ligne
 * et des entier naturels, bornés entre 0 et 100
 * En gros on a un tableau de 100 éléments dont chacun correspond à une valeur settable sur le mastergain
 */
@ToString
@AllArgsConstructor
public class NaturalVolumeControl {
    private final FloatControl innerVolumeControl;
    private final List<Float> mappingVolume;
    private int currentIndexVolume;


    public static NaturalVolumeControl from(SourceDataLine sourceDataLine) {
        return Optional.ofNullable(sourceDataLine)
                .filter(notNullSourceDataLine -> notNullSourceDataLine.isControlSupported(FloatControl.Type.MASTER_GAIN))
                .map(notNullSourceDataLine -> notNullSourceDataLine.getControl(FloatControl.Type.MASTER_GAIN))
                .map(FloatControl.class::cast)
                .map(volumeControl -> {

                    // récupération des bornes
                    var minValue = volumeControl.getMinimum();
                    var maxValue = volumeControl.getMaximum();
                    var currentValue = volumeControl.getValue();

                    // calcul des steps
                    var maxAmplitude = maxValue - minValue;
                    var stepSize = maxAmplitude / 99;

                    var steps = IntStream.range(0, 100)
                            .mapToObj(index -> minValue + stepSize * index)
                            .sorted()
                            .toList();

                    var currentIndexVolume = IntStream.range(0, 100)
                            .filter(index -> steps.get(index) >= currentValue)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("cant resolve current value as natural value"));

                    return new NaturalVolumeControl(volumeControl, steps, currentIndexVolume);
                }).orElse(new NoopNaturalVolumeControl());

    }

    public int getCurrentVolume() {
        return this.currentIndexVolume;
    }

    public void volumeDown() {
        if (this.currentIndexVolume - 2 >= 0) {
            this.currentIndexVolume -= 2;
            this.innerVolumeControl.setValue(this.mappingVolume.get(this.currentIndexVolume));
        }
    }

    public void volumeUp() {
        if (this.currentIndexVolume + 2 < 100) {
            this.currentIndexVolume += 2;
            this.innerVolumeControl.setValue(this.mappingVolume.get(this.currentIndexVolume));
        }
    }


    /**
     * gestion thread safe sur reinit: permet de tempérer jusqu'à ce que le controle soit récupéré
     */
    public static final class NoopNaturalVolumeControl extends NaturalVolumeControl {

        public NoopNaturalVolumeControl() {
            super(null, null, 50);
        }

        @Override
        public void volumeDown() {
        }

        @Override
        public void volumeUp() {
        }
    }



}
