package maroroma.homeserverng.kiosk.model.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllForeCasts {
    private CurrentForecast currentForecast;
    private List<Forecast> forecastsForTheCurrentDay;
    private List<DailyForecast> futureForeCasts;
}
