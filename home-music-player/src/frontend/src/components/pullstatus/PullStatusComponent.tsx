import {FC, useEffect} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {PlayerRequester} from "../../api/requesters/PlayerRequester";
import {StoppedPlayerAction} from "../../state/actions/playerActions/StoppedPlayerAction";
import {UpdateLastPlayerStatusAction} from "../../state/actions/playerActions/UpdateLastPlayerStatusAction";


const PullStatusComponent : FC = () => {

  const { dispatch } = useMusicPlayerContext();

  useEffect(() => {
      const intervalToRemove = setInterval(
        () =>
          PlayerRequester.getFullPlayerStatus().then((response) => {
            if (response.playerStatus === "STOPPED") {
              dispatch(new StoppedPlayerAction());
            } else {
              dispatch(new UpdateLastPlayerStatusAction(response));
            }
          }),
        500
      );
  
      PlayerRequester.getFullPlayerStatus().then((response) => {
        if (response.playerStatus === "STOPPED") {
          dispatch(new StoppedPlayerAction());
        } else {
          dispatch(new UpdateLastPlayerStatusAction(response));
        }
      });
  
      return () => clearInterval(intervalToRemove);
    }, []);

    return <></>
}

export default PullStatusComponent;