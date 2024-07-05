import {FC} from "react";
import {Track} from "../../../api/model/library/Track";


import "./TrackItemRenderer.css";


export type TrackItemRendererProps = {
    trackToDisplay: Track,
    onClick?: (track: Track) => void
}



const TrackItemRenderer: FC<TrackItemRendererProps> = ({ trackToDisplay, onClick = () => {} }) => {
    return <div onClick={() => { onClick(trackToDisplay) }} className="track-item-renderer clickable">
        <div className="track-name"> {trackToDisplay.trackNumber ? `${trackToDisplay.trackNumber.padStart(2, "0")} - ${trackToDisplay.name}` : trackToDisplay.name }</div>
    </div>
}

export default TrackItemRenderer;