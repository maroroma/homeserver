import React, { useEffect, useRef, useState } from 'react';
import bookApi from '../../apiManagement/BookApi';
import { when } from '../../tools/when';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import "./displayCover.scss";

export default function SerieRendererComponent({ serieToDisplay, onClick = () => { } }) {

    return <a className="collection-item avatar" onClick={onClick}>
        <img src={bookApi().resolveSeriePicture(serieToDisplay)} alt="" className="book-cover"></img>
        <span className="title">{serieToDisplay.title}</span>
        <p>{`${serieToDisplay.bookIds ? serieToDisplay.bookIds.length : 0} tomes`}</p>
        {serieToDisplay.completed ? <span className="secondary-content"><i className="material-icons">done_all</i></span> : <></>}
    </a>

}