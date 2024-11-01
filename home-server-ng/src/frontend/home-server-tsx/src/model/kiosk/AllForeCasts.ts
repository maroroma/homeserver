import CurrentForecast from "./CurrentForecast";
import DailyForecast from "./DailyForecast";
import Forecast from "./Forecast";

export default class AllForeCasts {

    static empty(): AllForeCasts {
        return new AllForeCasts(CurrentForecast.empty(), [], []);
    }

    constructor(
        public currentForecast: CurrentForecast,
        public forecastsForTheCurrentDay: Forecast[],
        public futureForeCasts: DailyForecast[]
    ) { }
}