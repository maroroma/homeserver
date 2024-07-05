import {FC} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ArrowRepeat, ArrowsFullscreen, ChevronDoubleLeft, ChevronDoubleRight, PauseCircle, PlayCircle, StopCircle} from "react-bootstrap-icons";

import "./SmallPlayerComponent.css";
import ScrollingTextComponent from "../common/scrollingtext/ScrollingTextComponent";
import {PlayerRequester} from "../../api/requesters/PlayerRequester";
import ThumbImage from "../common/ThumbImage";
import {DisplayFullscreenPlayerAction} from "../../state/actions/playerActions/DisplayFullscreenPlayerAction";
import {ViewState} from "../../state/ViewState";


const SmallPlayerComponent: FC = () => {

    const { playerSubState, dispatch, viewState } = useMusicPlayerContext();

    if (playerSubState.display !== "small" || viewState === ViewState.FullScreenPlayer) {
        return <></>
    }

    const resolveButtonFromStatus = () => {
        switch (playerSubState.lastPlayerStatus?.playerStatus) {
            case "LOADING":
                return <ArrowRepeat className="small-player-button endless-rotation"></ArrowRepeat>;
            case "PLAYING":
                return <PauseCircle className="clickable small-player-button" onClick={() => PlayerRequester.pausePlayer()} />;
            default:
                return <PlayCircle className="clickable small-player-button" onClick={() => PlayerRequester.resumePlayer()} />;
        }
    }


    return <div className="small-player-container hide-on-small-devices">
        <div className="small-player-component">
            <div className="small-player-buttons">
                <ThumbImage type="xsmall" className="small-player-thumb" libraryItemArts={playerSubState.lastPlayerStatus?.album.libraryItemArts} rounded={true} />
                <ChevronDoubleLeft className="small-player-button smaller-player-button clickable" onClick={() => PlayerRequester.previous()} />
                {
                    resolveButtonFromStatus()
                }
                <StopCircle className="small-player-button clickable" onClick={() => PlayerRequester.stopPlayer()} />

                <ChevronDoubleRight className="small-player-button smaller-player-button clickable" onClick={() => PlayerRequester.next()} />
                <ArrowsFullscreen className="clickable small-player-button smaller-player-button small-player-fullscreen-button" onClick={() => dispatch(new DisplayFullscreenPlayerAction())} />
            </div>
            <ScrollingTextComponent
                scrolling={playerSubState.lastPlayerStatus?.playerStatus === "PLAYING"}
                text={`${playerSubState.lastPlayerStatus?.artist.name} - ${playerSubState.lastPlayerStatus?.album.name} - ${playerSubState.lastPlayerStatus?.track.name}`}
            />
        </div>
    </div>

}

export default SmallPlayerComponent;