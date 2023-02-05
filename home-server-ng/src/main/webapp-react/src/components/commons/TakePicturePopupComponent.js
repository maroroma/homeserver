import React from 'react';
import { useState, useEffect, useRef } from 'react';
import { getPopupInstance } from './ModalPopupComponent';

import './TakePicturePopupComponent.scss'



export default function TakePicturePopupComponent({
    id,
    open,
    title,
    onPictureTaken = () => {},
    onNoPicture = () => { },
}) {


    const [innerPopupInstance, setInnerPopupInstance] = useState(undefined);

    const videoComponent = useRef(null);
    const canvasComponent = useRef(null);


    useEffect(() => {
        const instance = getPopupInstance(id);

        setInnerPopupInstance(instance);

        return () => {
            instance.destroy();
        }

    }, [id]);

    useEffect(() => {
        if (innerPopupInstance) {
            if (open) {
                innerPopupInstance.open();
                navigator.mediaDevices.getUserMedia({ video: true, audio: false })
                    .then(stream => videoComponent.current.srcObject = stream);
            } else {
                innerPopupInstance.close();
                videoComponent.current.srcObject.getTracks()[0].stop();
            }
        }
    }, [open]);





    const innerPopupInstanceClose = () => { };

    const noPictureClose = (result) => {
        innerPopupInstance.close();
        videoComponent.current.srcObject.getTracks()[0].stop();
        onNoPicture();
    };

    const takeAPictureAndClose = () => {
        canvasComponent.current.getContext('2d').drawImage(videoComponent.current, 0, 0, canvasComponent.current.width, canvasComponent.current.height);
        const image_data_url = canvasComponent.current.toDataURL('image/jpeg');
 
        // data url of the image
        console.log(image_data_url);
        onPictureTaken(image_data_url);
    }


    return (
        <div id={id} className="modal">
            <div className="modal-content">
                <h6>{title}</h6>
                <video id="video" width="320" height="240" autoPlay ref={videoComponent}></video>
                <canvas id="canvas" width="320" height="240" ref={canvasComponent}></canvas>
            </div>
            <div className="modal-footer">
                <a className="waves-effect waves-light btn-floating btn-large action-button" onClick={takeAPictureAndClose}><i className="material-icons left large">add_a_photo</i></a>
                <a className="action-button btn-floating btn-large red" onClick={() => noPictureClose(false)}>
                    <i className="large material-icons">undo</i>
                </a>
            </div>
        </div>
    );
}