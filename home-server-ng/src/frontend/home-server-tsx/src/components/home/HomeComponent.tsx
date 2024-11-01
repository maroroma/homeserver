import {FC, useEffect, useState} from "react";
import KioskRequester from "../../api/KioskRequester";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";
import AllForeCasts from "../../model/kiosk/AllForeCasts";
import EndWIPAction from "../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import TemperatureRenderer from "./TemperatureRenderer";
import WeatherCodeRenderer from "./WeatherCodeRenderer";
import WindRenderer from "./WindRenderer";
import HumidityRenderer from "./HumidityRenderer";

import "./HomeComponent.css";
import HourlyForecastRenderer from "./HourlyForecastRenderer";
import Forecast from "../../model/kiosk/Forecast";
import {Card, CardBody, CardTitle} from "react-bootstrap";
import {BootstrapText} from "../bootstrap/BootstrapText";
import DailyForecastRenderer from "./DailyForecastRenderer";
import DailyForecast from "../../model/kiosk/DailyForecast";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionUpdateButton from "../actionmenu/ActionUpdateButton";

const HomeComponent: FC = () => {

    const { dispatch } = useHomeServerContext();

    const [allForeCasts, setAllForeCasts] = useState(AllForeCasts.empty())



    const updateForeCast = () => {

        dispatch(new StartWIPAction("Récupération de la météo"));

        KioskRequester.getForecast()
            .then(response => {
                dispatch(new EndWIPAction());
                setAllForeCasts({
                    ...response,
                    forecastsForTheCurrentDay: response.forecastsForTheCurrentDay
                        .filter(Forecast.keepFuture())
                        .filter((aForecast, index) => index < 12)
                        .map(aForecast => Forecast.clone(aForecast)),
                    futureForeCasts: response.futureForeCasts
                        .filter(DailyForecast.keepFuture())
                        .filter((aDaily, index) => index < 4)
                        .map(future => DailyForecast.clone(future))
                });
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la récupération de la météo")))
    }




    useEffect(() => {
        const intervalToRemove = setInterval(
            () => { updateForeCast() },
            1800000);

        updateForeCast();

        return () => clearInterval(intervalToRemove);

    }, []);

    return <div>
        <Card>
            <CardBody>
                <CardTitle className={BootstrapText.Capitalize}><h2>Maintenant</h2></CardTitle>

                <div className="current-weather-wrapper">
                    <div className="weather-code">
                        <WeatherCodeRenderer weatherCode={allForeCasts.currentForecast.weatherCode} />
                    </div>
                    <div className="weather-temperature">
                        <TemperatureRenderer temperature={allForeCasts.currentForecast.temperature} />
                    </div>
                    <div className="weather-wind">
                        <WindRenderer windSpeed={allForeCasts.currentForecast.windSpeed} />
                    </div>
                    <div className="weather-humidity">
                        <HumidityRenderer humidity={allForeCasts.currentForecast.humidity} />
                    </div>
                </div>
            </CardBody>
        </Card>
        <Card>
            <CardBody>
                <CardTitle className={BootstrapText.Capitalize}><h2>prochaines heures</h2></CardTitle>
                <div className="hourly-weather-wrapper">
                    {allForeCasts.forecastsForTheCurrentDay.map((hourlyForeCast, index) => <HourlyForecastRenderer key={index} hourlyForeCast={hourlyForeCast} />)}
                </div>
            </CardBody>
        </Card>
        <Card>
            <CardBody>
                <CardTitle className={BootstrapText.Capitalize}><h2>prochains jours</h2></CardTitle>
                <div className="daily-weather-wrapper">
                    {allForeCasts.futureForeCasts.map((hourlyForeCast, index) => <DailyForecastRenderer key={index} dailyForecast={hourlyForeCast} />)}
                </div>
            </CardBody>
        </Card>
        <ActionMenuComponent>
            <ActionUpdateButton onClick={() => updateForeCast()} />
        </ActionMenuComponent>
    </div>;
}

export default HomeComponent;