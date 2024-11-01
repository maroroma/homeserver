package maroroma.homeserverng.kiosk.model.weather.meteofrance.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrentWeather {
    private String time;
    @JsonProperty("temperature_2m")
    private double temperature;
    @JsonProperty("relative_humidity_2m")
    private int relativeHumidity;
    @JsonProperty("apparent_temperature")
    private double apparentTemperature;
    @JsonProperty("is_day")
    private boolean isDay;
    private double precipitation;
    @JsonProperty("weather_code")
    private int weaterCode;
    @JsonProperty("wind_speed_10m")
    private double windSpeed;
}
