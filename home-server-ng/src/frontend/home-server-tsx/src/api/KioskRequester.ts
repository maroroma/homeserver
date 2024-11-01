import AllForeCasts from "../model/kiosk/AllForeCasts";
import {RequesterUtils} from "./RequesterUtils";

export default class KioskRequester {
    static getForecast(): Promise<AllForeCasts> {
        return RequesterUtils.get("/api/kiosk/weather/allforecasts")
    }
}