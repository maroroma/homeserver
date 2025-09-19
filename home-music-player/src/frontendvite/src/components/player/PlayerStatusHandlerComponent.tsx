import {type FC, useEffect} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {PlayerRequester} from "../../api/requesters/PlayerRequester";
import {UpdatePlayerStatusAction} from "../../state/actions/UpdatePlayerStatusAction";

const PlayerStatusHandlerComponent: FC = () => {

    const {dispatch} = useMusicPlayerContext();


    useEffect(() => {
        const scheduledToRemove = setInterval(() => {
            PlayerRequester.getFullPlayerStatus()
            .then(result => dispatch(new UpdatePlayerStatusAction(result)))

        }, 1000);

        return () => clearInterval(scheduledToRemove);

    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);



  return <></>;
};

export default PlayerStatusHandlerComponent;
