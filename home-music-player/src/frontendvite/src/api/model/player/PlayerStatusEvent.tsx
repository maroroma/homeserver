import { Album } from "../library/Album";
import { Artist } from "../library/Artist";
import { Track } from "../library/Track";
import { MemoryStatus } from "./MemoryStatus";

export type PlayerStatus = "PLAYING" | "PAUSED" | "STOPPED" | "LOADING";

export class PlayerStatusEvent {
  static empty(): PlayerStatusEvent {
    return new PlayerStatusEvent(
      "STOPPED",
      Track.empty(),
      Artist.empty(),
      Album.empty(),
      0,
      MemoryStatus.empty()
    );
  }
  constructor(
    public playerStatus: PlayerStatus,

    public track: Track,

    public artist: Artist,

    public album: Album,

    public volume: number,

    public memoryStatus: MemoryStatus

  ) { }
}
