import {FC, useEffect, useState} from "react"
import Forecast from "../../model/kiosk/Forecast"
import WeatherCodePictos from "../../model/kiosk/WeatherCodePictos"
import Temperatures from "../../model/kiosk/Temperatures"
import {BootstrapText} from "../bootstrap/BootstrapText"

import "./HourlyForecastRenderer.css";

export type HourlyForecastRendererProps = {
    hourlyForeCast: Forecast
}

const HourlyForecastRenderer: FC<HourlyForecastRendererProps> = ({ hourlyForeCast }) => {
    const [temperatureMatch, setTemperatureMatch] = useState(Temperatures.ALL_TEMPERATURES[2])
    useEffect(() => {
        setTemperatureMatch(Temperatures.findTemperature(hourlyForeCast.temperature));
    }, [hourlyForeCast]);
    return <div className="hourly-forecast-renderer">
        <div  className={BootstrapText.AlignCenter}>
            <h4>
                {WeatherCodePictos.findPicto(hourlyForeCast.weatherCode.wmoCode).icon}
                <span className={temperatureMatch.css}>{`${hourlyForeCast.temperature}°C`}</span>
            </h4>
        </div>
        <div className={BootstrapText.AlignCenter}>
            <h4>
                {hourlyForeCast.formatHour()}
            </h4>
        </div>
    </div>
}

export default HourlyForecastRenderer;