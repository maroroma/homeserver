package maroroma.homeserverng.kiosk.model.weather.meteofrance.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HourlyWeather {
    private List<String> time;
    @JsonProperty("weather_code")
    private List<Integer> weatherCode;
    @JsonProperty("temperature_2m")
    private List<Double> temperature;
    @JsonProperty("relative_humidity_2m")
    private List<Integer> relativeHumidity;
    @JsonProperty("wind_speed_10m")
    private List<Double> windSpeed;
}
