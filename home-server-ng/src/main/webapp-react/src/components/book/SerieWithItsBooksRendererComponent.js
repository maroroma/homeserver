import React from 'react';
import bookApi from '../../apiManagement/BookApi';

import "./displayCover.scss";
import "./SerieWithItsBooksRendererComponent.scss";
import BookFromSerieComponentRenderer from './BookFromSerieComponentRenderer';

export default function SerieWithItsBooksRendererComponent({ oneSerieWithBooks, onBookClick = () => { } }) {


    return <><a className="collection-item avatar purple lighten-5 serie-title">
        <img src={bookApi().resolveSeriePicture(oneSerieWithBooks)} alt="" className="book-cover"></img>
        <div className="title">{oneSerieWithBooks.title}</div>
        <div className="title">{`${oneSerieWithBooks.books.length} tomes`}</div>
    </a>
        <div className="serie-with-books-wrapper">
            {oneSerieWithBooks.books.sort((book1, book2) => {
                if (book1.serieInfo !== undefined && book1.serieInfo.orderInSerie !== undefined
                    && book2.serieInfo !== undefined && book2.serieInfo.orderInSerie !== undefined) {
                    return Number(book1.serieInfo.orderInSerie) - Number(book2.serieInfo.orderInSerie);
                } else {
                    return `${book1.title} ${book1.subtitle}`.localeCompare(`${book2.title} ${book2.subtitle}`);
                }

            }).map((oneBookResult, index) =>
                <BookFromSerieComponentRenderer key={index} oneBook={oneBookResult} onClick={() => onBookClick(oneBookResult)}></BookFromSerieComponentRenderer>)
                }
        </div></>

}