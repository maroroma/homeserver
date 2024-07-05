import {FC} from "react";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import FanartPanelComponent from "../common/panels/FanartPanelComponent";
import ThumbImage from "../common/ThumbImage";
import ScrollingTextComponent from "../common/scrollingtext/ScrollingTextComponent";
import {ArrowRepeat, ChevronDoubleLeft, ChevronDoubleRight, PauseCircle, PlayCircle, StopCircle, VolumeDown, VolumeUp} from "react-bootstrap-icons";
import {PlayerRequester} from "../../api/requesters/PlayerRequester";
import CircularSlider from "@fseehawer/react-circular-slider";

import "./FullscreenPlayerComponent.css";
import {Button, InputGroup} from "react-bootstrap";
import HeaderMenuComponent from "../common/menu/HeaderMenuComponent";
import MenuAllArtistsButton from "../common/menu/MenuAllArtistsButton";
import {LeaveFullScreenAction} from "../../state/actions/playerActions/LeaveFullScreenAction";
import {SelectArtistAction} from "../../state/actions/artistViewActions/SelectArtistAction";
import {LibraryRequester} from "../../api/requesters/LibraryRequester";
import {DisplayAlbumAction} from "../../state/actions/allTracksActions/DisplayAlbumAction";


// https://www.npmjs.com/package/@fseehawer/react-circular-slider


const FullscreenPlayerComponent: FC = () => {

    const { playerSubState, dispatch } = useMusicPlayerContext();

    const goToCurrentArtistAlbumList = () => {
        if (playerSubState.lastPlayerStatus?.artist) {
            LibraryRequester.getArtist(playerSubState.lastPlayerStatus.artist.id)
                .then(artist => dispatch(new SelectArtistAction(artist)));
        }
    }

    const loadTracksForAlbum = () => {
        if (playerSubState.lastPlayerStatus?.album) {
            const albumToLoader = playerSubState.lastPlayerStatus?.album;

            LibraryRequester.getTracksForAlbum(playerSubState.lastPlayerStatus?.album)
                .then(tracks => dispatch(new DisplayAlbumAction(albumToLoader, tracks)));
        }
    }

    const resolveButtonFromStatus = () => {
        switch (playerSubState.lastPlayerStatus?.playerStatus) {
            case "LOADING":
                return <ArrowRepeat className="small-player-button endless-rotation"></ArrowRepeat>;
            case "PLAYING":
                return <PauseCircle className="clickable fullscreen-player-button" onClick={() => PlayerRequester.pausePlayer()} />;
            default:
                return <PlayCircle className="clickable fullscreen-player-button" onClick={() => PlayerRequester.resumePlayer()} />;
        }
    }

    return <>

        <HeaderMenuComponent>
            <MenuAllArtistsButton onClick={() => { dispatch(new LeaveFullScreenAction()) }} />
        </HeaderMenuComponent>
        <div className="fullscreen-player-central-grid">
            <div className="fullscreen-player-artist clickable" onClick={() => goToCurrentArtistAlbumList()}>
                <ThumbImage libraryItemArts={playerSubState.lastPlayerStatus?.artist.libraryItemArts} type="large" rounded={true} />
                <h1>{playerSubState.lastPlayerStatus?.artist.name}</h1>
            </div>
            <div className="fullscreen-player-volume fullscreen-player-volume-buttons">
                <div className="fullscreen-player-volume-buttons fullscreen-player-volume-buttons-left">
                    <VolumeDown className="clickable" onClick={() => PlayerRequester.volumeDown()}></VolumeDown>
                </div>
                <div className="fullscreen-player-volume-gauge">
                    <CircularSlider
                        label="Volume"
                        min={0}
                        max={100}
                        dataIndex={playerSubState.lastPlayerStatus?.volume}
                        labelColor="white"
                        labelBottom={true}
                        hideKnob={true}
                        knobSize={72}
                        valueFontSize="100px"
                        labelFontSize="25px"
                        progressColorFrom="black"
                        progressColorTo="black"
                        progressSize={18}
                        trackColor="white"
                        trackSize={24}
                        trackDraggable={false}
                        knobDraggable={false}
                    >
                    </CircularSlider>
                </div>
                <h1>{playerSubState.lastPlayerStatus?.volume}</h1>
                <div className="fullscreen-player-volume-buttons fullscreen-player-volume-buttons-right">
                    <VolumeUp className="clickable" onClick={() => PlayerRequester.volumeUp()}></VolumeUp>
                </div>
            </div>
            <div className="fullscreen-player-album clickable" onClick={() => loadTracksForAlbum()}>
                <ThumbImage libraryItemArts={playerSubState.lastPlayerStatus?.album.libraryItemArts} type="large" rounded={true} />
                <h1>{playerSubState.lastPlayerStatus?.album.name}</h1>
            </div>
        </div>
        {/* uniquement en small device */}
        <div className="fullscreen-player-smalldevices album-thumb clickable" onClick={() => loadTracksForAlbum()}>
            <ThumbImage rounded={true} libraryItemArts={playerSubState.lastPlayerStatus?.album.libraryItemArts} type="description" className="large-thumb-override large-thumb-default-override" />
        </div>
        <div className="fullscreen-player-scrolling-text">
            <ScrollingTextComponent text={`${playerSubState.lastPlayerStatus?.track.name}`} scrolling={playerSubState.lastPlayerStatus?.playerStatus === "PLAYING"} />
        </div>
        {/* uniquement en small device */}
        <div className="fullscreen-player-volume-smalldevices">
            <div className="album-and-artist-names">
                <h1 className="clickable" onClick={() => loadTracksForAlbum()}>{playerSubState.lastPlayerStatus?.album.name}</h1>
                <h2 className="clickable" onClick={() => goToCurrentArtistAlbumList()}>{playerSubState.lastPlayerStatus?.artist.name}</h2>
            </div>
            <InputGroup className="fullscreen-player-volume-smalldevices-buttons">
                <Button variant="light" size="lg" className="volume-down">
                    <VolumeDown className="clickable" size={50} onClick={() => PlayerRequester.volumeDown()}></VolumeDown>
                </Button>
                <div className="fullscreen-player-volume-smalldevices-value">{playerSubState.lastPlayerStatus?.volume}</div>
                <Button variant="light" size="lg" className="volume-up">
                    <VolumeUp className="clickable" size={50} onClick={() => PlayerRequester.volumeUp()}></VolumeUp>
                </Button>
            </InputGroup>
        </div>
        <div className="fullscreen-player-buttons">
            <ChevronDoubleLeft className="clickable fullscreen-player-button" onClick={() => PlayerRequester.previous()} />
            {
                resolveButtonFromStatus()
            }
            <StopCircle className="clickable fullscreen-player-button" onClick={() => PlayerRequester.stopPlayer()} />

            <ChevronDoubleRight className=" clickable fullscreen-player-button" onClick={() => PlayerRequester.next()} />
        </div>
        <FanartPanelComponent fanart={playerSubState.lastPlayerStatus?.artist.libraryItemArts}></FanartPanelComponent>
    </>
}

export default FullscreenPlayerComponent;