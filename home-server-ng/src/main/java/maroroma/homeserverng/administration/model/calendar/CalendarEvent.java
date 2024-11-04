package maroroma.homeserverng.administration.model.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {

    public static CalendarEvent none() {
        return CalendarEvent.builder()
                .build();
    }

    private String id;
    private CalendarFixedDate startDate;
    private CalendarFixedDate endDate;
    private String description;
    private String utf8IconsCode;
    private boolean dayEvent;

    public boolean matches() {

        if (this.dayEvent) {
            return startDate.isToday();
        }

        return startDate.isBeforeToday() && endDate.isAfterToday();
    }
}
