import {FC} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import {ArrowRepeat, ArrowsFullscreen, ChevronDoubleLeft, ChevronDoubleRight, PauseCircle, PlayCircle, StopCircle} from "react-bootstrap-icons";

import "./SmallPlayerComponent.css";
import ScrollingTextComponent from "../common/scrollingtext/ScrollingTextComponent";
import {PlayerRequester} from "../../api/requesters/PlayerRequester";
import ThumbImage from "../common/ThumbImage";
import {DisplayFullscreenPlayerAction} from "../../state/actions/playerActions/DisplayFullscreenPlayerAction";
import {ViewState} from "../../state/ViewState";
import CssTools from "../common/CssTools";
import PlayerStatusTool from "../common/PlayerStatusTool";


const SmallPlayerComponent: FC = () => {

    const { playerSubState, dispatch, viewState } = useMusicPlayerContext();

    if (playerSubState.display !== "small" || viewState === ViewState.FullScreenPlayer) {
        return <></>
    }

    const resolveButtonFromStatus = () => {
        switch (playerSubState.lastPlayerStatus?.playerStatus) {
            case "LOADING":
                return <ArrowRepeat className="small-player-button endless-rotation disable"></ArrowRepeat>;
            case "PLAYING":
                return <PauseCircle className="clickable small-player-button" onClick={() => PlayerRequester.pausePlayer()} />;
            default:
                return <PlayCircle className="clickable small-player-button blinkable" onClick={() => PlayerRequester.resumePlayer()} />;
        }
    }


    return <div className="small-player-container hide-on-small-devices">
        <div className="small-player-component">
            <div className="small-player-buttons">
                <ThumbImage type="xsmall" className="small-player-thumb" libraryItemArts={playerSubState.lastPlayerStatus?.album.libraryItemArts} rounded={true} />
                <ChevronDoubleLeft
                    className={CssTools.smallPlayerButton(playerSubState)}
                    onClick={() =>
                        PlayerStatusTool.of(playerSubState).then(() => PlayerRequester.previous())
                    }
                />
                {
                    resolveButtonFromStatus()
                }
                <StopCircle
                    className={CssTools.smallPlayerButton(playerSubState)}
                    onClick={() =>
                        PlayerStatusTool.of(playerSubState).then(() => PlayerRequester.stopPlayer())
                    }
                />

                <ChevronDoubleRight
                    className={CssTools.smallPlayerButton(playerSubState)}
                    onClick={() =>
                        PlayerStatusTool.of(playerSubState).then(() => PlayerRequester.next())
                    }
                />
                <ArrowsFullscreen className="clickable small-player-button smaller-player-button small-player-fullscreen-button" onClick={() => dispatch(new DisplayFullscreenPlayerAction())} />
            </div>
            <ScrollingTextComponent
                scrolling={PlayerStatusTool.of(playerSubState).shouldScroll()}
                text={PlayerStatusTool.of(playerSubState).smallPlayerScrollingText()}
                blinking={PlayerStatusTool.of(playerSubState).shouldBlink()}
            />
        </div>
    </div>

}

export default SmallPlayerComponent;