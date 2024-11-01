package maroroma.homeserverng.kiosk.model.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * https://open-meteo.com/en/docs/meteofrance-api#latitude=47.2751&longitude=-2.2179&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset&timezone=Europe%2FBerlin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Forecast {
    private String dateTime;
    private double temperature;
    private int humidity;
    private double totalPrecipitation;
    private double windSpeed;
    // https://gist.github.com/stellasphere/9490c195ed2b53c707087c8c2db4ec0c
    private WeatherCode weatherCode;
}
