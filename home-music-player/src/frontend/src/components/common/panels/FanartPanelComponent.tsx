import {FC} from "react";
import {LibraryItemArts} from "../../../api/model/library/LibraryItemArts";
import {Image} from "react-bootstrap";

import "./FanartPanelComponent.css";


export type FanartDisplayMode = "background" | "player"

export type FanartPanelComponentProps = {
    fanart: LibraryItemArts | undefined,
    mode?: FanartDisplayMode
}



const FanartPanelComponent: FC<FanartPanelComponentProps> = ({ fanart, mode = "background" }) => {

    if (!fanart || fanart === null || fanart.fanartPath === null || !fanart.fanartPath) {
        return <></>
    }


    return <div className={`fanart-panel fanart-mode-${mode}`}>
        <Image src={`musicplayer/localresources/fanarts/${fanart.fanartPath}`} ></Image>
    </div>

}

export default FanartPanelComponent;