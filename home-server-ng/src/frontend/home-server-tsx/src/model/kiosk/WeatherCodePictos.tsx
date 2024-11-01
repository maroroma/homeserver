import {ReactElement} from "react";
import {Cloud, CloudDrizzle, CloudHail, CloudHaze, CloudLightningRain, CloudRain, CloudSlash, CloudSleet, CloudSnow, CloudSun, Sun} from "react-bootstrap-icons";
import {CustomClassNames} from "../../components/bootstrap/CssTools";

export default class WeatherCodePictos {

    static readonly ALL_WEATHER_CODE_PICTOS = [
        new WeatherCodePictos(<Sun className={CustomClassNames.SpaceAfterIcon}/>, [0, 1]),
        new WeatherCodePictos(<CloudSun className={CustomClassNames.SpaceAfterIcon}/>, [2]),
        new WeatherCodePictos(<Cloud className={CustomClassNames.SpaceAfterIcon}/>, [3]),
        new WeatherCodePictos(<CloudHaze className={CustomClassNames.SpaceAfterIcon}/>, [45]),
        new WeatherCodePictos(<CloudDrizzle className={CustomClassNames.SpaceAfterIcon}/>, [51, 53, 55, 56, 57]),
        new WeatherCodePictos(<CloudHail className={CustomClassNames.SpaceAfterIcon}/>, [61, 61, 66, 80]),
        new WeatherCodePictos(<CloudRain className={CustomClassNames.SpaceAfterIcon}/>, [63, 65, 67, 81, 82]),
        new WeatherCodePictos(<CloudSleet className={CustomClassNames.SpaceAfterIcon}/>, [71, 77, 85]),
        new WeatherCodePictos(<CloudSnow className={CustomClassNames.SpaceAfterIcon}/>, [73, 75, 86]),
        new WeatherCodePictos(<CloudLightningRain className={CustomClassNames.SpaceAfterIcon}/>, [95, 96, 99]),
    ]

    static unknown(): WeatherCodePictos {
        return new WeatherCodePictos(<CloudSlash className={CustomClassNames.SpaceAfterIcon}/>, []);
    }

    static findPicto(weatherCode: number): WeatherCodePictos {
        return WeatherCodePictos.ALL_WEATHER_CODE_PICTOS.find(aPicto => aPicto.matchingCodes.includes(weatherCode)) ?? WeatherCodePictos.unknown();
    }


    constructor(public icon: ReactElement, public matchingCodes: number[]) { }
}