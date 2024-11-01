import {ReactElement} from "react";
import {ThermometerHalf, ThermometerHigh, ThermometerLow, ThermometerSnow} from "react-bootstrap-icons";

export default class Temperatures {
    constructor(public min: number, public max: number, public css: string, public icon: ReactElement) { }

    static readonly ALL_TEMPERATURES = [
        new Temperatures(-50, 3, "temperature-extra-cold", <ThermometerSnow />),
        new Temperatures(3, 10, "temperature-cold", <ThermometerLow />),
        new Temperatures(10, 17, "temperature-medium", <ThermometerHalf />),
        new Temperatures(17, 23, "temperature-warm", <ThermometerHigh />),
        new Temperatures(23, 100, "temperature-hot", <ThermometerHigh />),
    ]

    static findTemperature(temperature: number): Temperatures {
        return Temperatures.ALL_TEMPERATURES.find(aTemperature => aTemperature.matches(temperature)) ??
            Temperatures.ALL_TEMPERATURES[2];
    }

    matches(temperature: number): boolean {
        return temperature > this.min && temperature <= this.max;
    }
}