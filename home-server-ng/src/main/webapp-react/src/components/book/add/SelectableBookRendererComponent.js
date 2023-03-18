import React from 'react';
import bookApi from '../../../apiManagement/BookApi';
import CheckBoxComponent from '../../commons/CheckBoxComponent';

export default function SelectableBookRendererComponent({ bookToDisplay, useInitialImageLink = false, onClick = () => { } }) {

    return <a className="collection-item avatar" onClick={onClick}>
        <img src={useInitialImageLink ? bookToDisplay.initialImageLink : (bookApi().downloadBaseUrl() + "/" + bookToDisplay.id + "/picture")} alt="" className="circle"></img>
        <span className="title">{bookToDisplay.title} {bookToDisplay.subtitle}</span>
        <p>{bookToDisplay.author}</p>
        <span className="secondary-content">
            <CheckBoxComponent dataswitch={bookToDisplay.selected}></CheckBoxComponent>
        </span>
    </a>

}