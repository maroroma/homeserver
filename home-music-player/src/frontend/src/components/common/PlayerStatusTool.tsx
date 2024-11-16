import {PlayerSubState} from "../../state/PlayerSubState";
import CssTools from "./CssTools";

export default class PlayerStatusTool {

    constructor(private playerStatus: PlayerSubState) { }

    static of(playerStatus: PlayerSubState): PlayerStatusTool {
        return new PlayerStatusTool(playerStatus);
    }

    then(action: () => void) {
        if (this.playerStatus.isLoading !== true) {
            return action();
        }
    }

    fullScreenScrollingText(): string {

        if (this.playerStatus.isLoading) {
            return "Chargement en cours";
        }

        if (this.playerStatus.lastPlayerStatus) {
            return this.playerStatus.lastPlayerStatus.track.name;
        }

        return "";
    }

    smallPlayerScrollingText(): string {
        if (this.playerStatus.isLoading) {
            return "Chargement en cours";
        }

        if (this.playerStatus.lastPlayerStatus) {
            return `${this.playerStatus.lastPlayerStatus?.artist.name} - ${this.playerStatus.lastPlayerStatus?.album.name} - ${this.playerStatus.lastPlayerStatus?.track.name}`;
        }

        return "";
    }

    shouldScroll(): boolean {
        return this.playerStatus.isLoading ||
            this.playerStatus.lastPlayerStatus?.playerStatus === "PLAYING"
    }

    shouldBlink(): boolean {
        if (this.playerStatus.lastPlayerStatus) {
            return this.playerStatus.lastPlayerStatus.playerStatus === "PAUSED";
        }
        return false;
    }

    css(elseCss?: string): CssTools {
        return CssTools.of().ifElse(this.playerStatus.isLoading, "disable", elseCss ? elseCss : "");
    }
}