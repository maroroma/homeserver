package maroroma.homeserverng.kiosk.model.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyForecast {
    private String date;
    private WeatherCode weatherCode;
    private double temperatureMin;
    private double temperatureMax;
}
