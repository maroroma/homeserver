import React from 'react';

import "./BookForListRenderer.scss";
import bookApi from '../../../apiManagement/BookApi';
import {editBookComponentEventReactor} from '../EditBookComponent';

export default function BookForListRenderer({ oneBook, onClick }) {

    const openPopupForEditingOneBook = () => {
        editBookComponentEventReactor().openEditPopup(oneBook);
    }

    return <>
        <div className="card book-big-screen">
            <div className="card-image">
                <img src={(bookApi().downloadBaseUrl() + "/" + oneBook.id + "/picture")}></img>
                <a className="btn-floating halfway-fab waves-effect waves-light" onClick={openPopupForEditingOneBook}>
                    <i className="material-icons">edit</i>
                </a>
            </div>
            <div className="card-content">
                <p className="center-align">{oneBook.subtitle}</p>
            </div>
        </div>

        <div className="card book-small-screen">
            <a onClick={openPopupForEditingOneBook} href="#!">
                <div className="card-image">
                    <img src={(bookApi().downloadBaseUrl() + "/" + oneBook.id + "/picture")}></img>
                    <span className="card-title book-title">{oneBook.subtitle}</span>
                </div>
            </a>
        </div>
    </>

}