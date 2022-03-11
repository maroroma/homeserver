package maroroma.homeserverng.iot.model.scheduler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.administration.model.Task;

import java.util.concurrent.ScheduledFuture;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ScheduledTask {
    private ScheduledFuture<?> scheduledFuture;
    private String id;
    private String name;

}
