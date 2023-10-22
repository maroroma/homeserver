import React from 'react';
import bookApi from '../../apiManagement/BookApi';

import "./displayCover.scss";
import "./SerieWithItsBooksRendererComponent.scss";
import BookFromSerieComponentRenderer from './BookFromSerieComponentRenderer';
import mofRouterEventReactor from '../mainmenu/MOFRouterEventReactor';
import modulesAdapter from '../mainmenu/ModulesAdapter';

export default function SerieWithItsBooksRendererComponent({ oneSerieWithBooks, onBookClick = () => { } }) {


    const addBookToSerie = () => {
        console.log("addBookToSerie", oneSerieWithBooks);
        mofRouterEventReactor().selectedModuleChange(modulesAdapter().getMenuDescriptorForPath("/books/import"), `serieId=${oneSerieWithBooks.id}`);
    }

    return <><div className="collection-item avatar purple lighten-5 serie-title">
        <img src={bookApi().resolveSeriePicture(oneSerieWithBooks)} alt="" className="book-cover"></img>
        <div className="title">{oneSerieWithBooks.title}</div>
        <div className="title">{`${oneSerieWithBooks.books.length} tomes`}</div>
        <a href="#!" className="secondary-content" onClick={addBookToSerie}><i className="material-icons">playlist_add</i></a>
    </div>
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