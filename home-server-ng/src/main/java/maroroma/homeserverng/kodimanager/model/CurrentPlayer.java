package maroroma.homeserverng.kodimanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentPlayer {
    private String playerId;
    private float percentage;
    private Duration currentTime;
    private Duration totalTime;
    private String title;
}
