import {PlayerSubState} from "../../state/PlayerSubState";
import CssTools from "./CssTools";

export default class PlayerStatusTool {

    constructor(private isLoading: boolean) { }

    static isLoading(playerStatus: PlayerSubState): PlayerStatusTool {
        return new PlayerStatusTool(playerStatus.lastPlayerStatus !== undefined && playerStatus.lastPlayerStatus.playerStatus === "LOADING");
    }

    then(action: () => void) {
        if (this.isLoading !== true) {
            return action();
        }
    }

    css(elseCss?: string): CssTools {
        return CssTools.of().ifElse(this.isLoading, "disable", elseCss ? elseCss : "");
    }
}