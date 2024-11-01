import WeatherCode from "./WeatherCode";

export default class Forecast {


    static keepCurrentDay(): (f: Forecast) => boolean {
        return aForecast => new Date().getDate() === Forecast.normalizeForecastDate(aForecast).getDate()
    }

    static keepFuture(): (f: Forecast) => boolean {
        const now = new Date();
        return aForecast => now.getTime() < Forecast.normalizeForecastDate(aForecast).getTime();
    }

    static normalizeForecastDate(foreCast: Forecast): Date {
        return new Date(`${foreCast.dateTime}`);
    }

    formatHour(): string {

        const normalizeFormatDate = Forecast.normalizeForecastDate(this);
        const formatter = new Intl.DateTimeFormat('fr-FR', { hour:"2-digit", minute:"2-digit" });

        return formatter.format(normalizeFormatDate);
        // return `${normalizeFormatDate.getHours()}`.padStart(2, "0")
    }


    // xp pour récup un vrai objet en sortie d'appel http
    // c pas ouf faut vraiment creuser pour arrêter de tricker avec des statiques
    static clone(foreCast: Forecast): Forecast {
        return new Forecast(foreCast.dateTime, foreCast.temperature, foreCast.humidity, foreCast.totalPrecipitation, foreCast.windSpeed, foreCast.weatherCode)
    }



    constructor(
        public dateTime: string,
        public temperature: number,
        public humidity: number,
        public totalPrecipitation: number,
        public windSpeed: number,
        public weatherCode: WeatherCode
    ) {
        this.formatHour = this.formatHour.bind(this);
    }
}