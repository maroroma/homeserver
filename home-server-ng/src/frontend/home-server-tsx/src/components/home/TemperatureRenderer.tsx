import {FC, useEffect, useState} from "react"
import Temperatures from "../../model/kiosk/Temperatures"

import "./TemperatureRenderer.css";

export type TemperatureRendererProps = {
    temperature: number
}



const TemperatureRenderer: FC<TemperatureRendererProps> = ({ temperature }) => {


    const [temperatureMatch, setTemperatureMatch] = useState<Temperatures>(Temperatures.ALL_TEMPERATURES[2])

    useEffect(() => {
        setTemperatureMatch(Temperatures.findTemperature(temperature));
    }, [temperature]);


    return <h1 className={temperatureMatch.css}>{temperatureMatch.icon} {`${temperature}°C`} </h1>


}

export default TemperatureRenderer