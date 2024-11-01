package maroroma.homeserverng.kiosk.model.weather.meteofrance.api;

import lombok.Data;

@Data
public class MeteoFranceResponse {
    private CurrentWeather current;
    private HourlyWeather hourly;
    private DailyWeather daily;
}
