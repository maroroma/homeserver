import React, {useEffect, useState} from 'react';

import "./ImageViewerComponent.scss";
import {FileExtensions} from '../../../tools/fileTypes';
import keys from '../../../tools/keys';


function ImageViewerComponent({ display = false, imageBaseUrl = "", imageUrlList = [], selectedStartupIndex = 0, onClose = () => { } }) {

    const [imageToDisplay, setImageToDisplay] = useState("");
    const [imageToDisplayIndex, setImageToDisplayIndex] = useState(0);

    useEffect(() => {
        setImageToDisplayIndex(selectedStartupIndex);
    }, [imageUrlList, selectedStartupIndex]);


    useEffect(() => {
        setImageToDisplay(imageUrlList[imageToDisplayIndex]);
        const keyEventHandler = (event) => {

            keys(event).onEscape(() => {
                onClose();
            });

            keys(event).onRight(() => {
                onNext();
            });

            keys(event).onLeft(() => {
                onPrevious();
            });

            keys(event).onBottom((keyEvent) => {
                keyEvent.preventDefault();
                onNext();
            });

            keys(event).onUp((keyEvent) => {
                keyEvent.preventDefault();
                onPrevious();
            })
        }

        keys().register(keyEventHandler);


        return () => {
            keys().unregister(keyEventHandler);
        }
    }, [imageToDisplayIndex, imageUrlList]);



    const onNext = () => {
        let nextIndexValue;
        if (imageToDisplayIndex < imageUrlList.length - 1) {
            nextIndexValue = imageToDisplayIndex + 1;
        } else {
            nextIndexValue = 0;
        }
        setImageToDisplayIndex(nextIndexValue);
    };

    const onPrevious = () => {
        let nextIndexValue;
        if (imageToDisplayIndex === 0) {
            nextIndexValue = imageUrlList.length -1;
        } else {
            nextIndexValue = imageToDisplayIndex -1;
        }

        setImageToDisplayIndex(nextIndexValue);

    }



    return display ? <>
        <div className="image-viewer-overlay valign-wrapper">
            <a className="btn-small waves-effect waves-light close-button" href="#!" onClick={() => onClose()}><i className="material-icons">close</i></a>
            <div className="image-viewer-grid">
                <div className="valign-wrapper center-align side-panel">
                    <a className="btn-floating btn waves-effect waves-light red nav-button" href="#!" onClick={() => onPrevious()}>
                        <i className="material-icons">navigate_before</i>
                    </a>
                </div>
                <div className="image-container">
                    <img
                        alt=""
                        className="image-to-display clickable"
                        src={`${imageBaseUrl}/${imageToDisplay}`}
                        onClick={() => onNext()}
                    ></img>
                </div>
                <div className="valign-wrapper center-align  side-panel">
                    <a className="btn-floating btn waves-effect waves-light red  nav-button" href="#!" onClick={() => onNext()}>
                        <i className="material-icons">navigate_next</i>
                    </a>
                </div>
            </div>
            <div className="image-viewer-list-description">
                <div className="image-position-description">
                    {imageToDisplayIndex + 1} / {imageUrlList.length}
                </div>
            </div>
        </div>

    </> : <></>
}

function isImageViewerCompatibleFile(fileToBeTested) {
    return FileExtensions.IMAGES.match(fileToBeTested.name);
}

export {
    ImageViewerComponent,
    isImageViewerCompatibleFile
}