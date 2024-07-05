import {PlayerStatusEvent} from "../api/model/player/PlayerStatusEvent"

export type PlayerDisplayMode = "none" | "small" | "fullscreen" 

export type PlayerSubState = {
    display: PlayerDisplayMode
    lastPlayerStatus?: PlayerStatusEvent
}