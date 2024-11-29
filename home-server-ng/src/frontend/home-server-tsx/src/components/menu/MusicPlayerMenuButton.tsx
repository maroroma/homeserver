import {FC, useEffect, useState} from "react";
import {Nav} from "react-bootstrap";
import {DiscFill} from "react-bootstrap-icons";
import MusicPlayerStatus from "../../model/musicplayer/MusicPlayerStatus";
import MusicPlayerRequester from "../../api/MusicPlayerRequester";
import CssTools from "../bootstrap/CssTools";
import {PlayerStatus} from "../../model/musicplayer/PlayerStatus";


const MusicPlayerMenuButton: FC = () => {



    const [musicPlayerStatus, setMusicPlayerStatus] = useState(MusicPlayerStatus.empty())

    useEffect(() => {
        const intervalToRemove = setInterval(
            () => MusicPlayerRequester
                .getMusicPlayerStatus()
                .then(response =>
                    setMusicPlayerStatus(response)
                )
                .catch(error => console.log("Erreur rencontrée lors de la récupération du status du musicPlayer"))
            ,
            2000);

        MusicPlayerRequester.getMusicPlayerStatus()
            .then(response => setMusicPlayerStatus(response))
            .catch(error => console.log("Erreur rencontrée lors de la récupération du status du musicPlayer"))

        return () => clearInterval(intervalToRemove);
    }, []);


    return <Nav.Link href={musicPlayerStatus.musicPlayerUrl} target="_blank">
        <DiscFill className={CssTools
            .of("space-after-icon")
            .if(musicPlayerStatus.playerStatus === PlayerStatus.PLAYING, "endless-rotation")
            .if(musicPlayerStatus.playerStatus === PlayerStatus.PAUSED, "blinkable")
            .css()} size={20} />
        MusicPlayer
    </Nav.Link>

}

export default MusicPlayerMenuButton;