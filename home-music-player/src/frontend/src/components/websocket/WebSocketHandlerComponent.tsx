import {FC, useState} from "react";
import {StompSessionProvider, useSubscription} from "react-stomp-hooks";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {DisplayToastAction} from "../../state/actions/toastActions/DisplayToastAction";
import {UpdateLastPlayerStatusAction} from "../../state/actions/playerActions/UpdateLastPlayerStatusAction";
import {StoppedPlayerAction} from "../../state/actions/playerActions/StoppedPlayerAction";


/**
 * Centralisation de la gestion des events réceptionnés à travers websockets
 * @returns 
 */
const WebSocketHandlerComponent: FC = () => {

    const { dispatch } = useMusicPlayerContext();

    console.log("REACT_APP_WEBSOCKET_URL", process.env.REACT_APP_WEBSOCKET_URL)


    return <StompSessionProvider
        // onConnect={() => console.log("onConnect")}
        // onDisconnect={() => console.log("onDisconnect")}
        // onChangeState={() => console.log("onChangeState")}
        // url={'http://localhost:8080/ws-endpoint'}>
        url={`${process.env.REACT_APP_WEBSOCKET_URL}/ws-endpoint`}>
        <div style={{ color: "white" }}>
            <SubscribingComponent></SubscribingComponent>
        </div>
    </StompSessionProvider>
}

function SubscribingComponent() {

    const { dispatch } = useMusicPlayerContext();

    const [message, setMessage] = useState("");
    // Subscribe to the topic that we have opened in our spring boot app
    useSubscription('/topic/player-status', (message) => { 
        // console.log(message.body);
        setMessage(message.body) 
        dispatch(new UpdateLastPlayerStatusAction(JSON.parse(message.body)));
    });
    useSubscription('/topic/player-stopped', (message) => { 
        // setMessage(message.body) 
        dispatch(new StoppedPlayerAction());
    });
    useSubscription('/topic/notifications', (message) => { 

        dispatch(DisplayToastAction.fromNotication(JSON.parse(message.body)));

        // dispatch(new StoppedPlayerAction());
    });

    return (
        <></>
        // <div> The broadcast message from websocket broker is {message}</div>
    )
}

export default WebSocketHandlerComponent;