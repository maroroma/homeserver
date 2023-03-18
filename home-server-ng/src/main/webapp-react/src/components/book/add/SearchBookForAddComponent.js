import React, {useState} from 'react';
import bookApi from '../../../apiManagement/BookApi';
import {when} from '../../../tools/when';
import toaster from '../../commons/Toaster';
import {addBookSubReactor} from './AddBookSubReactor';

import TakePicturePopupComponent from '../../commons/TakePicturePopupComponent'

export default function SearchBookForAddComponent() {


    const [isbnString, setIsbnString] = useState("");
    const [basicQueryString, setBasicQueryString] = useState("");
    const [displayCameraPopup, setDisplayCameraPopup] = useState(false);

    const updateIsbnString = (event) => {
        setIsbnString(event.target.value);
    };
    const updateBasicQueryString = (event) => {
        setBasicQueryString(event.target.value);
    };


    const searchBookByIsbnNumber = () => {
        bookApi()
            .findBooksByIsbn(isbnString)
            .then(result => addBookSubReactor().searchResultReceived(result));
    };

    const searchBookByGenericQuery = () => {
        bookApi()
            .findBooksByGenericQuery(basicQueryString)
            .then(result => addBookSubReactor().searchResultReceived(result));
    };

    const onKeyDownHandlerForGenericQuery = (key) => {
        if (key.keyCode === 13) {
            searchBookByGenericQuery();
        }
    };
    const onKeyDownHandlerForIsbn = (key) => {
        if (key.keyCode === 13) {
            searchBookByIsbnNumber();
        }
    };

    const openCameraPopup = () => {
        console.log("openCameraPopup");
        setDisplayCameraPopup(true);
    }

    const noPictureSoClosePopup = () => {
        console.log("closeCameraPopup")
        setDisplayCameraPopup(false);
    }

    const sendImageToScanAsIsbn = (base64Image) => {
        bookApi()
            .findBooksByIsbnPicture(base64Image)
            .then(result => {
                if (result.scannedIsbn === "NO ISBN SCANNED") {
                    toaster().plopWarning("Code isbn non trouvé sur la photo")
                }
                addBookSubReactor().searchResultReceived(result.books);
                setDisplayCameraPopup(false);
            });
    }






    return <div>

        <div>
            <div>
                <a className="waves-effect waves-light btn-floating btn-large" onClick={openCameraPopup}><i className="material-icons left large">add_a_photo</i></a>
            </div>
            <div className="row">
                <div className="input-field col s9">
                    <i className="material-icons prefix">input</i>
                    <input id="title_input" type="text" className="validate" value={basicQueryString} onChange={updateBasicQueryString} onKeyDown={onKeyDownHandlerForGenericQuery}></input>
                    <label htmlFor="title_input">Titre, Auteur...</label>
                </div>
                <div className="input-field col s3">
                    <a className={when(basicQueryString === "").thenDisableElement("waves-effect waves-light btn")} onClick={searchBookByGenericQuery} href="#"><i className="material-icons">search</i></a>
                </div>
            </div>
            <div className="row">
                <div className="input-field col s9">
                    <i className="material-icons prefix">book</i>
                    <input id="isbn_input" type="text" className="validate" value={isbnString} onChange={updateIsbnString} onKeyDown={onKeyDownHandlerForIsbn}></input>
                    <label htmlFor="isbn_input">ISBN</label>
                </div>
                <div className="input-field col s3">
                    <a className={when(isbnString === "").thenDisableElement("waves-effect waves-light btn")} onClick={searchBookByIsbnNumber} href="#"><i className="material-icons">search</i></a>
                </div>
            </div>
        </div>



        <TakePicturePopupComponent
            open={displayCameraPopup}
            id={"take_picture_from_isbn_popup"}
            title="Scanner un code barre"
            onNoPicture={noPictureSoClosePopup}
            onPictureTaken={sendImageToScanAsIsbn}
        >
        </TakePicturePopupComponent>


    </div>
}