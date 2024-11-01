import {FC, useEffect, useState} from "react"
import WeatherCodePictos from "../../model/kiosk/WeatherCodePictos"
import Temperatures from "../../model/kiosk/Temperatures"
import {BootstrapText} from "../bootstrap/BootstrapText"

import "./DailyForecastRenderer.css";
import DailyForecast from "../../model/kiosk/DailyForecast"
import CssTools from "../bootstrap/CssTools"

export type DailyForecastRendererProps = {
    dailyForecast: DailyForecast
}

const DailyForecastRenderer: FC<DailyForecastRendererProps> = ({ dailyForecast }) => {

    const [temperatureMinMatch, setTemperatureMinMatch] = useState(Temperatures.ALL_TEMPERATURES[2])
    const [temperatureMaxMatch, setTemperatureMaxMatch] = useState(Temperatures.ALL_TEMPERATURES[2])


    useEffect(() => {
        setTemperatureMinMatch(Temperatures.findTemperature(dailyForecast.temperatureMin));
        setTemperatureMaxMatch(Temperatures.findTemperature(dailyForecast.temperatureMax));
    }, [dailyForecast]);
    return <div className="daily-forecast-renderer-wrapper">
        <div className={CssTools.of("daily-picto").then(BootstrapText.AlignCenter).css()}>
            <h4>
                {WeatherCodePictos.findPicto(dailyForecast.weatherCode.wmoCode).icon}
            </h4>
        </div>
        <div className="daily-min">
            <h5 className={temperatureMinMatch.css}>{`${dailyForecast.temperatureMin}°C`}</h5>
        </div>
        <div className="daily-max">
            <h5 className={temperatureMaxMatch.css}>{`${dailyForecast.temperatureMax}°C`}</h5>
        </div>
        <div className={CssTools.of("daily-day").then(BootstrapText.AlignCenter).then(BootstrapText.Capitalize).css()}>
            <h5>{dailyForecast.formatWeekday()}</h5>
        </div>
    </div>
}

export default DailyForecastRenderer;