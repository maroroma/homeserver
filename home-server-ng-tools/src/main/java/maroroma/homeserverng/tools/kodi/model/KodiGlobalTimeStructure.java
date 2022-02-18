package maroroma.homeserverng.tools.kodi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KodiGlobalTimeStructure {
    private int hours;
    private int milliseconds;
    private int minutes;
    private int seconds;
//                "hours": 0,
//                        "milliseconds": 963,
//                        "minutes": 1,
//                        "seconds": 38

    @JsonIgnore
    public Duration convertToDuration() {
        return Duration.ofHours(this.hours).plusMinutes(this.minutes).plusSeconds(this.seconds).plusMillis(this.milliseconds);
    }
}
