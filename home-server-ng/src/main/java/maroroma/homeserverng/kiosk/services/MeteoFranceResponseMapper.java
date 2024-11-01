package maroroma.homeserverng.kiosk.services;

import lombok.RequiredArgsConstructor;
import maroroma.homeserverng.kiosk.model.weather.AllForeCasts;
import maroroma.homeserverng.kiosk.model.weather.CurrentForecast;
import maroroma.homeserverng.kiosk.model.weather.DailyForecast;
import maroroma.homeserverng.kiosk.model.weather.Forecast;
import maroroma.homeserverng.kiosk.model.weather.meteofrance.api.CurrentWeather;
import maroroma.homeserverng.kiosk.model.weather.meteofrance.api.DailyWeather;
import maroroma.homeserverng.kiosk.model.weather.meteofrance.api.HourlyWeather;
import maroroma.homeserverng.kiosk.model.weather.meteofrance.api.MeteoFranceResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@Component
@RequiredArgsConstructor
public class MeteoFranceResponseMapper {

    private final WeatherCodesResolver weatherCodesResolver;


    public AllForeCasts map(MeteoFranceResponse response) {
        return AllForeCasts.builder()
                .currentForecast(map(response.getCurrent()))
                .forecastsForTheCurrentDay(map(response.getHourly()))
                .futureForeCasts(map(response.getDaily()))
                .build();
    }

    private CurrentForecast map(CurrentWeather currentWeather) {
        return CurrentForecast.currentForeCastBuilder()
                .apparentTemperature(currentWeather.getApparentTemperature())
                .temperature(currentWeather.getTemperature())
                .dateTime(currentWeather.getTime())
                .humidity(currentWeather.getRelativeHumidity())
                .windSpeed(currentWeather.getWindSpeed())
                .weatherCode(weatherCodesResolver.mapWeatherCode(currentWeather.getWeaterCode()))
                .build();
    }

    private List<Forecast> map(HourlyWeather hourlyWeather) {

        return ListExtractor.stream(hourlyWeather, HourlyWeather::getTime)
                .map(extractor -> Forecast.builder()
                        .dateTime(extractor.extract(HourlyWeather::getTime))
                        .temperature(extractor.extractDouble(HourlyWeather::getTemperature))
                        .windSpeed(extractor.extractDouble(HourlyWeather::getWindSpeed))
                        .weatherCode(weatherCodesResolver.mapWeatherCode(extractor.extractInt(HourlyWeather::getWeatherCode)))
                        .humidity(extractor.extractInt(HourlyWeather::getRelativeHumidity))
                        .build())
                .toList();
    }

    private List<DailyForecast> map(DailyWeather dailyWeather) {
        return ListExtractor.stream(dailyWeather, DailyWeather::getTime)
                .map(extractor -> DailyForecast.builder()
                        .date(extractor.extract(DailyWeather::getTime))
                        .weatherCode(weatherCodesResolver.mapWeatherCode(extractor.extractInt(DailyWeather::getWeatherCode)))
                        .temperatureMin(extractor.extractDouble(DailyWeather::getTemperatureMin))
                        .temperatureMax(extractor.extractDouble(DailyWeather::getTemperatureMax))
                        .build())
                .toList();
    }


    @RequiredArgsConstructor
    static class ListExtractor<T> {
        private final T itemWithLists;
        private final int index;

        <U> U extract(Function<T, List<U>> listSupplier) {
            return  listSupplier.apply(itemWithLists).get(index);
        }

        double extractDouble(Function<T, List<Double>> listSupplier) {
            return Optional.ofNullable(this.<Double>extract(listSupplier)).orElse(0d);
        }

        int extractInt(Function<T, List<Integer>> listSupplier) {
            return Optional.ofNullable(this.<Integer>extract(listSupplier)).orElse(0);
        }

        static <U> Stream<ListExtractor<U>> stream(U itemWithLists, Function<U, List<?>> referenceListExtractor) {
            return IntStream.range(0, referenceListExtractor.apply(itemWithLists).size())
                    .mapToObj(index -> new ListExtractor<>(itemWithLists, index));
        }
    }


}
