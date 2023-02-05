import React, { useEffect, useRef, useState } from 'react';
import bookApi from '../../apiManagement/BookApi';
import SimpleBookRendererComponent from './SimpleBookRendererComponent';

import "./displayCover.scss";

export default function SerieWithItsBooksRendererComponent({oneSerieWithBooks, onBookClick = () => {}}) {



    return <><a className="collection-item avatar purple lighten-5 serie-title">
        <img src={bookApi().resolveSeriePicture(oneSerieWithBooks)} alt="" className="book-cover"></img>
        <span className="title">{oneSerieWithBooks.title}</span>
    </a>
        {oneSerieWithBooks.books.map((oneBookResult, index) =>
            <SimpleBookRendererComponent
                bookToDisplay={oneBookResult}
                key={index}
                useInitialImageLink={false}
                onClick={() => onBookClick(oneBookResult)}
                otherProperty={"book-into-serie"}
            ></SimpleBookRendererComponent>)}
    </>


}