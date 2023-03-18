import React from 'react';
import bookApi from '../../apiManagement/BookApi';
import SimpleBookRendererComponent from './SimpleBookRendererComponent';

import "./displayCover.scss";

export default function SerieWithItsBooksRendererComponent({oneSerieWithBooks, onBookClick = () => {}}) {



    return <><a className="collection-item avatar purple lighten-5 serie-title">
        <img src={bookApi().resolveSeriePicture(oneSerieWithBooks)} alt="" className="book-cover"></img>
        <span className="title">{oneSerieWithBooks.title}</span>
    </a>
        {oneSerieWithBooks.books.sort((book1, book2) => {
            if (book1.serieInfo !== undefined && book1.serieInfo.orderInSerie !==undefined
                 && book2.serieInfo !== undefined && book2.serieInfo.orderInSerie !==undefined) {
                    return Number(book1.serieInfo.orderInSerie) - Number(book2.serieInfo.orderInSerie);
            } else {
                return `${book1.title} ${book1.subtitle}`.localeCompare(`${book2.title} ${book2.subtitle}`);
            }

        }).map((oneBookResult, index) =>
            <SimpleBookRendererComponent
                bookToDisplay={oneBookResult}
                key={index}
                useInitialImageLink={false}
                onClick={() => onBookClick(oneBookResult)}
                otherProperty={"book-into-serie"}
            ></SimpleBookRendererComponent>)}
    </>


}