export default class WeatherCode {

    static empty(): WeatherCode {
        return new WeatherCode(0, "", "");
    }

    constructor(public wmoCode: number, public description: string, public imageUrl: string) {

    }
}