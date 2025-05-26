import MusicPlayerStatus from "../../../model/musicplayer/MusicPlayerStatus";
import {HomeServerRootState} from "../../states/HomeServerRootState";
import {HomeServerAction} from "../HomeServerAction";

export default class UpdateMusicPlayerAction implements HomeServerAction {
  constructor(private status: MusicPlayerStatus) {}

  applyToState(previousState: HomeServerRootState): HomeServerRootState {
    return {
      ...previousState,
      musicPlayerSubState: {
        musicPlayerStatus: this.status,
      },
    };
  }
}
