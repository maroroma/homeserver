package maroroma.homeserverng.kiosk.model.weather;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * https://open-meteo.com/en/docs/meteofrance-api#latitude=47.2751&longitude=-2.2179&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,wind_speed_10m&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset&timezone=Europe%2FBerlin
 */
@Data
@NoArgsConstructor
public class CurrentForecast extends Forecast {
    private double apparentTemperature;

    @Builder(builderMethodName = "currentForeCastBuilder")
    public CurrentForecast(String dateTime, double temperature, int humidity, double totalPrecipitation, double windSpeed, WeatherCode weatherCode, double apparentTemperature) {
        super(dateTime, temperature, humidity, totalPrecipitation, windSpeed, weatherCode);
        this.apparentTemperature = apparentTemperature;
    }
}
