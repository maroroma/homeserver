package maroroma.homeserverng.iot.model.scheduler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerFrequency {
    private String name;
    private Duration period;

    public enum Frequencies {

        ONE_MINUTE(SchedulerFrequency.builder()
                .name("toutes les minutes")
                .period(Duration.ofMinutes(1))
                .build());

        public SchedulerFrequency frequency() {
            return this.innerFrequency;
        }

        public Duration period() {
            return this.innerFrequency.getPeriod();
        }

        private SchedulerFrequency innerFrequency;

        Frequencies(SchedulerFrequency innerFrequency) {
            this.innerFrequency = innerFrequency;
        }

    }

}
