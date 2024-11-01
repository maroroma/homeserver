import WeatherCode from "./WeatherCode";

export default class DailyForecast {


    static normalizeDailyForecastDate(foreCast: DailyForecast): Date {
        return new Date(`${foreCast.date}`);
    }

    static clone(source: DailyForecast): DailyForecast {
        return new DailyForecast(source.date, source.weatherCode, source.temperatureMin, source.temperatureMax);
    }

    static keepFuture(): (f: DailyForecast) => boolean {
        const now = new Date();
        return aForecast => now.getTime() < DailyForecast.normalizeDailyForecastDate(aForecast).getTime();
    }

    



    constructor(public date: string, public weatherCode: WeatherCode, public temperatureMin: number, public temperatureMax: number) { }


    formatWeekday(): string {

        const normalizeFormatDate = DailyForecast.normalizeDailyForecastDate(this);
        const formatter = new Intl.DateTimeFormat('fr-FR', { weekday:"long" });

        return formatter.format(normalizeFormatDate);
        // return `${normalizeFormatDate.getHours()}`.padStart(2, "0")
    }
}