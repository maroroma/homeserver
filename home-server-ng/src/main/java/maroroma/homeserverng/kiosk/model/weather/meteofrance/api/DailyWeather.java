package maroroma.homeserverng.kiosk.model.weather.meteofrance.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeather {
    private List<String> time;
    @JsonProperty("weather_code")
    private List<Integer> weatherCode;
    @JsonProperty("temperature_2m_max")
    private List<Double> temperatureMax;
    @JsonProperty("temperature_2m_min")
    private List<Double> temperatureMin;
}
