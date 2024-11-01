import {FC} from "react"
import WeatherCode from "../../model/kiosk/WeatherCode"
import WeatherCodePictos from "../../model/kiosk/WeatherCodePictos"
import {BootstrapText} from "../bootstrap/BootstrapText"

export type WeatherCodeRendererProps = {
    weatherCode: WeatherCode
}

const WeatherCodeRenderer: FC<WeatherCodeRendererProps> = ({ weatherCode }) => {
    return <h1 className={BootstrapText.Capitalize}>{WeatherCodePictos.findPicto(weatherCode.wmoCode).icon}<span>{weatherCode.description}</span></h1>
}

export default WeatherCodeRenderer;