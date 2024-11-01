package maroroma.homeserverng.kiosk.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.kiosk.model.weather.AllForeCasts;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final MeteoFranceResponseMapper meteoFranceResponseMapper;
    private final MeteoFranceClient meteoFranceClient;

    public AllForeCasts getAllForecasts() {
        var meteoFranceResponse = meteoFranceClient.getWeather();
        return this.meteoFranceResponseMapper.map(meteoFranceResponse);
    }
}
