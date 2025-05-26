import {FC, useEffect} from "react";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import MusicPlayerRequester from "../../api/MusicPlayerRequester";
import UpdateMusicPlayerAction from "../../context/actions/musicplayer/UpdateMusicPlayerAction";


const BackgroundTasksComponent : FC = () => {


    const {dispatch} = useHomeServerContext();


    useEffect(() => {
        const intervalToRemove = setInterval(
            () => MusicPlayerRequester
                .getMusicPlayerStatus()
                .then(response =>
                    dispatch(new UpdateMusicPlayerAction(response))
                )
                .catch(error => console.log("Erreur rencontrée lors de la récupération du status du musicPlayer"))
            ,
            2000);

        MusicPlayerRequester.getMusicPlayerStatus()
            .then(response => dispatch(new UpdateMusicPlayerAction(response)))
            .catch(error => console.log("Erreur rencontrée lors de la récupération du status du musicPlayer"))

        return () => clearInterval(intervalToRemove);
    }, []);




    return <></>

}

export default BackgroundTasksComponent;