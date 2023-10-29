import React from 'react';
import {FileExtensions} from '../../../tools/fileTypes';
import "./MusicPlayerComponent.scss";

function MusicPlayerComponent({ display = false, onClose = () => { }, musicBaseUrl = "", fileToPlay = {}, autoPlay = true }) {

    return display ?
        <>
            <div className="music-player-overlay valign-wrapper center-align">
                <a className="btn-small waves-effect waves-light close-button" href="#!" onClick={() => onClose()}><i className="material-icons">close</i></a>

                <div className="center-align music-player-content">
                    <div className="music-player-file-description center-align">
                        <div className="music-player-file-name center-align pink accent-3 valign-wrapper">
                            <i className="material-icons left">music_video</i>
                            <span>{fileToPlay.name}</span>
                        </div>
                    </div>
                    <div className="music-player-audio-control">
                        <audio
                            controls={true}
                            autoPlay={autoPlay}
                            src={`${musicBaseUrl}/${fileToPlay.id}`}></audio>
                    </div>
                </div>
            </div>
        </>
        : <></>
}

function isMusicPlayerCompatibleFile(fileToBeTested) {
    return FileExtensions.MUSIC.match(fileToBeTested.name);
}


export {
    MusicPlayerComponent,
    isMusicPlayerCompatibleFile
}