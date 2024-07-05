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

    private final boolean persistent;

    public static SimpleBroadcastNotification error(String message) {
        return SimpleBroadcastNotification.builder().level(LEVEL.ERROR).message(message).build();
    }

    public static SimpleBroadcastNotification info(String message) {
        return SimpleBroadcastNotification.builder().level(LEVEL.INFO).message(message).build();
    }

    public static SimpleBroadcastNotification waiting(String message) {
        return SimpleBroadcastNotification.builder().level(LEVEL.INFO).message(message).persistent(true).build();
    }


    public enum LEVEL {
        INFO,
        ERROR
    }
}
