import Forecast from "./Forecast";
import WeatherCode from "./WeatherCode";

export default class CurrentForecast extends Forecast {

    static empty(): CurrentForecast {
        return new CurrentForecast(0, "", 0, 0, 0, 0, WeatherCode.empty());
    }


    constructor(
        public apparentTemperature: number,
        public dateTime: string,
        public temperature: number,
        public humidity: number,
        public totalPrecipitation: number,
        public windSpeed: number,
        public weatherCode: WeatherCode
    ) {
        super(dateTime, temperature, humidity, totalPrecipitation, windSpeed, weatherCode);
    }
}