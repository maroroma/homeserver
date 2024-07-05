import {Album} from "../library/Album";
import {Artist} from "../library/Artist";
import {Track} from "../library/Track";

export type PlayerStatus = "PLAYING" | "PAUSED" | "STOPPED" | "LOADING"


export class PlayerStatusEvent {
    constructor(
        public playerStatus: PlayerStatus,

        public track: Track,

        public artist: Artist,

        public album: Album,

        public volume: number) { }
}