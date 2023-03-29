import React from 'react';
import bookApi from '../../apiManagement/BookApi';

import "./BookFromSerieComponentRenderer.scss";

export default function BookFromSerieComponentRenderer({ oneBook, onClick }) {

    return <>
        <div className="card book-big-screen">
            <div className="card-image">
                <img src={(bookApi().downloadBaseUrl() + "/" + oneBook.id + "/picture")}></img>
                <a className="btn-floating halfway-fab waves-effect waves-light" onClick={onClick}>
                    <i className="material-icons">edit</i>
                </a>
            </div>
            <div className="card-content">
                <p className="center-align">{oneBook.subtitle}</p>
            </div>
        </div>

        <div className="card book-small-screen">
            <a onClick={onClick}>
                <div className="card-image">
                    <img src={(bookApi().downloadBaseUrl() + "/" + oneBook.id + "/picture")}></img>
                </div>
                <div className="card-content">
                    <p className="center-align">{oneBook.subtitle}</p>
                </div>
            </a>
        </div>
    </>

}