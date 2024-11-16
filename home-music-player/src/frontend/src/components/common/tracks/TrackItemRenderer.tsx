import {FC} from "react";
import {Track} from "../../../api/model/library/Track";


import "./TrackItemRenderer.css";
import {useMusicPlayerContext} from "../../../state/MusicPlayerContext";
import CssTools from "../CssTools";


export type TrackItemRendererProps = {
    trackToDisplay: Track,
    onClick?: (track: Track) => void
}



const TrackItemRenderer: FC<TrackItemRendererProps> = ({ trackToDisplay, onClick = () => { } }) => {


    const { playerSubState } = useMusicPlayerContext();


    return <div
        onClick={() => { if (playerSubState.isLoading !== true) { onClick(trackToDisplay) } }}
        className={CssTools.of("track-item-renderer").disableOnLoading(playerSubState, "clickable").css()}
    >
        <div className={CssTools.of("track-name").disableOnLoading(playerSubState).css()}> {trackToDisplay.trackNumber ? `${trackToDisplay.trackNumber.padStart(2, "0")} - ${trackToDisplay.name}` : trackToDisplay.name}</div>
    </div>
}

export default TrackItemRenderer;