package maroroma.homemusicplayer.model.messaging.api;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@Builder
public class SimpleBroadcastNotification {
    private final String message;
    private final LEVEL level;

    public static SimpleBroadcastNotification error(String message) {
        return SimpleBroadcastNotification.builder().level(LEVEL.ERROR).message(message).build();
    }


    public enum LEVEL {
        INFO,
        ERROR
    }
}
