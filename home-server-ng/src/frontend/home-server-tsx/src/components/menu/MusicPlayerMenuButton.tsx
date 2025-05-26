import {FC} from "react";
import {Nav} from "react-bootstrap";
import {DiscFill} from "react-bootstrap-icons";
import CssTools from "../bootstrap/CssTools";
import {PlayerStatus} from "../../model/musicplayer/PlayerStatus";
import {useHomeServerContext} from "../../context/HomeServerRootContext";


const MusicPlayerMenuButton: FC = () => {


    const {musicPlayerSubState} = useHomeServerContext();


    return <Nav.Link href={musicPlayerSubState.musicPlayerStatus.musicPlayerUrl} target="_blank">
        <DiscFill className={CssTools
            .of("space-after-icon")
            .if(musicPlayerSubState.musicPlayerStatus.playerStatus === PlayerStatus.PLAYING, "endless-rotation")
            .if(musicPlayerSubState.musicPlayerStatus.playerStatus === PlayerStatus.PAUSED, "blinkable")
            .css()} size={20} />
        MusicPlayer
    </Nav.Link>

}

export default MusicPlayerMenuButton;