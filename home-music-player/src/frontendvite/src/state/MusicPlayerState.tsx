import type { PlayerStatusEvent } from "../api/model/player/PlayerStatusEvent";
import type { EmbeddedPlayerState } from "./EmbeddedPlayerState";
import type { ToastState } from "./ToastState";
import type { MusicPlayerContextAction } from "./actions/MusicPlayerContextActions";

export type MusicPlayerState = {
  dispatch: React.Dispatch<MusicPlayerContextAction>;
  toastState: ToastState;
  isScrollOnTop: boolean;
  playerStatus: PlayerStatusEvent;
  embeddedPlayerState: EmbeddedPlayerState;
};
