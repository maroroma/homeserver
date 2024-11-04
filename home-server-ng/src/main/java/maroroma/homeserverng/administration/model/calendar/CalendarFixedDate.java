package maroroma.homeserverng.administration.model.calendar;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarFixedDate {
    private int month;
    private int day;


    @JsonIgnore
    public boolean isAfterToday() {
        return asLocalDate().isAfter(LocalDate.now()) || isToday();
    }

    @JsonIgnore
    public boolean isBeforeToday() {
        return asLocalDate().isBefore(LocalDate.now()) || isToday();
    }

    @JsonIgnore
    public boolean isToday() {
        return asLocalDate().isEqual(LocalDate.now());
    }

    @JsonIgnore
    private LocalDate asLocalDate() {
        return LocalDate.of(LocalDate.now().getYear(), this.month, this.day);
    }
}
