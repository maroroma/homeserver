import React from 'react';
import bookApi from '../../apiManagement/BookApi';

import "./SimpleBookRendererComponent.scss";
import "./displayCover.scss";

export default function SimpleBookRendererComponent({ bookToDisplay, onClick = () => { }, otherProperty: additionnalStyle} ) {

    return <a className={`collection-item avatar ${additionnalStyle}`} onClick={onClick}>
        <img src={(bookApi().downloadBaseUrl() + "/" + bookToDisplay.id + "/picture")} alt="" className="book-cover"></img>
        <span className="title">{bookToDisplay.title} {bookToDisplay.subtitle}</span>
        <p>{bookToDisplay.author}</p>
        { bookToDisplay.serieInfo?.serieName !== undefined ? <span className="badge secondary-content">{bookToDisplay.serieInfo?.serieName}</span> : <></> }
    </a>

}