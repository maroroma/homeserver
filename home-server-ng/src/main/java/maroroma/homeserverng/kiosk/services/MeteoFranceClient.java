package maroroma.homeserverng.kiosk.services;

import maroroma.homeserverng.kiosk.model.weather.meteofrance.api.MeteoFranceResponse;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.config.HomeServerPropertyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MeteoFranceClient {
    @Property("homeserver.kiosk.weather.api.url")
    HomeServerPropertyHolder weatherApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public MeteoFranceResponse getWeather() {
        return restTemplate.getForObject(this.weatherApiUrl.getResolvedValue(), MeteoFranceResponse.class);
    }

}
