import {FC} from "react";
import {Button} from "react-bootstrap";
import {DiscFill} from "react-bootstrap-icons";

import "./MenuPlayerRunning.css"
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import {DisplayFullscreenPlayerAction} from "../../../state/actions/playerActions/DisplayFullscreenPlayerAction";
import {ViewState} from "../../../state/ViewState";

const MenuPlayerRunning: FC = () => {

    const {dispatch, viewState} = useMusicPlayerContext();

    if (viewState === ViewState.FullScreenPlayer) {
        return <></>
    }


    return <Button color="red" size="lg" variant="light" onClick={() => dispatch(new DisplayFullscreenPlayerAction())}><DiscFill className="menu-player-running endless-rotation" size={30} /></Button>
}

export default MenuPlayerRunning;
