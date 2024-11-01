package maroroma.homeserverng.kiosk.services;

import maroroma.homeserverng.kiosk.model.weather.WeatherCode;
import maroroma.homeserverng.tools.annotations.InjectNanoRepository;
import maroroma.homeserverng.tools.annotations.Property;
import maroroma.homeserverng.tools.repositories.NanoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static maroroma.homeserverng.kiosk.services.MeteoCacheNeeder.WEATHER_CODES_CACHE_NAME;

@Component
public class WeatherCodesResolver {

    @InjectNanoRepository(
            file = @Property("homeserver.kiosk.weather.weathercodes.store"),
            idField = "wmoCode",
            persistedType = WeatherCode.class)
    private NanoRepository weatherCodesRepo;


    @Cacheable(cacheNames = WEATHER_CODES_CACHE_NAME)
    public WeatherCode mapWeatherCode(int weatherCode) {
        return weatherCodesRepo.<WeatherCode>findById(weatherCode).orElse(null);
    }
}
