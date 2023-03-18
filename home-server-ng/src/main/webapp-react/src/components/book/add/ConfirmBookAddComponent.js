import React, {useEffect, useState} from 'react';
import sort from '../../../tools/sort';
import SerieRendererComponent from '../SerieRendererComponent';
import {addBookSubReactor} from './AddBookSubReactor';


export default function ConfirmBookAddComponent() {

    const [requestToDisplay, setRequestToDisplay] = useState({});


    useEffect(() => {
        const unsubscribeRequestUpdated = addBookSubReactor().onBookAddRequestUpdated(updatedRequest => {
            setRequestToDisplay(updatedRequest);
        });

        return () => {
            unsubscribeRequestUpdated();
        }

    }, []);


    const serieBloc = requestToDisplay.serieToAssociateTo ?
        <>
            <a className="collection-header"><h5>Série associée</h5></a>
            <SerieRendererComponent serieToDisplay={requestToDisplay.serieToAssociateTo}></SerieRendererComponent>
        </> : <></>;

    const editOneItem = (bookToEdit, fieldToEdit, newValue) => {
        const updatedBooksToAdd = requestToDisplay.booksToAdd
            .map(oneBook => {
                if (oneBook.id !== bookToEdit.id) {
                    return oneBook;
                } else {
                    oneBook[fieldToEdit] = newValue;
                    return oneBook;
                }
            });

        addBookSubReactor().bookAddRequestUpdated({
            ...requestToDisplay,
            booksToAdd: updatedBooksToAdd
        });
    };

    const editSerieOrder = (bookToEdit, newValue) => {
        const updatedBooksToAdd = requestToDisplay.booksToAdd
            .map(oneBook => {
                if (oneBook.id !== bookToEdit.id) {
                    return oneBook;
                } else {
                    if (oneBook.serieInfo === undefined || oneBook.serieInfo === null) {
                        return {
                            ...oneBook,
                            serieInfo: {
                                orderInSerie: newValue
                            }
                        };
                    } else {
                        oneBook.serieInfo.orderInSerie = newValue;
                        return oneBook;
                    }
                }
            });

        console.log(updatedBooksToAdd);

        addBookSubReactor().bookAddRequestUpdated({
            ...requestToDisplay,
            booksToAdd: updatedBooksToAdd
        });
    }


    return <div>
        <div className="collection">
            <a className="collection-header"><h4>{requestToDisplay.booksToAdd?.length === 1 ? "Livre sélectionné" : `${requestToDisplay.booksToAdd?.length} Livres sélectionnés`} </h4></a>
            {requestToDisplay.booksToAdd?.sort(sort().basic(oneBook => oneBook.title)).map((oneBookResult, index) =>

                <div key={index}>
                    <a className="collection-item avatar">
                        <img src={oneBookResult.initialImageLink} alt="" className="circle"></img>
                        <div className="input-field">
                            <input placeholder="Titre" id="book_title" type="text" className="validate"
                                value={oneBookResult.title}
                                onChange={event => editOneItem(oneBookResult, "title", event.target.value)}></input>
                            <input placeholder="Sous Titre" id="book_subtitle" type="text" className="validate"
                                value={oneBookResult.subtitle}
                                onChange={event => editOneItem(oneBookResult, "subtitle", event.target.value)}></input>
                            <input placeholder="Auteur" id="book_author" type="text" className="validate"
                                value={oneBookResult.author}
                                onChange={event => editOneItem(oneBookResult, "author", event.target.value)}></input>
                            <input placeholder="A qui c'est ?" id="owner" type="text" className="validate"
                                value={oneBookResult.owner}
                                onChange={event => editOneItem(oneBookResult, "owner", event.target.value)}></input>
                            {requestToDisplay.serieToAssociateTo ? <input placeholder="Order dans la série" id="order_in_serie" type="text" className="validate"
                                value={oneBookResult?.serieInfo?.orderInSerie}
                                onChange={event => editSerieOrder(oneBookResult, event.target.value)}
                            ></input> : <></>}
                        </div>
                    </a>
                </div>)}
            {serieBloc}

        </div>
    </div>;
}