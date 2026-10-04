import type { ApplicationStats } from "../model/administration/ApplicationStats";
import { RequesterUtils } from "./RequesterUtils";

export class AdministrationRequester {
    public static getApplicationStats(): Promise<ApplicationStats> {
        return RequesterUtils.get("/api/musicplayer/administration/stats");
    }
}
