import React, {useEffect, useState} from 'react';
import bookApi from '../../../apiManagement/BookApi';

import "../displayCover.scss";
import "./ProgressiveSerieRenderer.scss";
import mofRouterEventReactor from '../../mainmenu/MOFRouterEventReactor';
import modulesAdapter from '../../mainmenu/ModulesAdapter';
import {useDisplayList} from '../../../tools/displayList';
import {when} from '../../../tools/when';
import BookForListRenderer from './BookForListRenderer';
import SimplerYesNoModalPopupComponent from '../../commons/SimplerYesNoModalPopupComponent';


export default function ProgressiveSerieRenderer({ serieToDisplay, onSave = () => { }, onDelete = () => {} }) {

    const [booksLoading, setBooksLoading] = useState(false);
    const [displayBooks, setDisplayBooks] = useState(false);
    const [booksLoaded, setBooksLoaded] = useState(false);
    const [allBooksForSerie, setAllBooksForSerie] = useDisplayList();
    const [displayChangeSerieStatusPopup, setDisplayChangeSerieStatusPopup] = useState(false);
    const [displayDeletePopup, setDisplayDeletePopup] = useState(false);
    const [paddedVolumes, setPaddedVolumes] = useState("00");



    const changeSerieStatusPopupCloseHandler = (result) => {
        setDisplayChangeSerieStatusPopup(false);
        if (result !== serieToDisplay.completed) {
            onSave({ ...serieToDisplay, completed: result });
        }
    }

    const displayChangeSerieStatusPopupForSerie = () => {
        setDisplayChangeSerieStatusPopup(true);
    }
    const displayDeletePopupHandler = () => {
        setDisplayDeletePopup(true);
    }


    const deleteSeriePopupCloseHandler = (result) => {
        setDisplayDeletePopup(false);
        if (result === true) {
            onDelete(serieToDisplay);
        }
    }


    const addBookToSerie = () => {
        mofRouterEventReactor().selectedModuleChange(modulesAdapter().getMenuDescriptorForPath("/books/import"), `serieId=${serieToDisplay.id}`);
    }

    const switchDisplayBooks = () => {

        if (displayBooks === false) {
            if (booksLoaded === false) {
                setBooksLoading(true);
                bookApi().getAllBooksFromSerie(serieToDisplay.id)
                    .then(result => {
                        setAllBooksForSerie(
                            {
                                ...allBooksForSerie.update(result).updateSort((book1, book2) => {
                                    if (book1.serieInfo !== undefined && book1.serieInfo.orderInSerie !== undefined
                                        && book2.serieInfo !== undefined && book2.serieInfo.orderInSerie !== undefined) {
                                        return Number(book1.serieInfo.orderInSerie) - Number(book2.serieInfo.orderInSerie);
                                    } else {
                                        return `${book1.title} ${book1.subtitle}`.localeCompare(`${book2.title} ${book2.subtitle}`);
                                    }
                                })
                            }
                        );
                        setBooksLoading(false);
                        setBooksLoaded(true);
                        setDisplayBooks(true);
                    })
            } else {
                setDisplayBooks(true);
            }
        } else {
            setDisplayBooks(false);
        }
    }



    // on ne charge que si le composant est visible, et une seule fois
    useEffect(() => {
        setDisplayBooks(false);
        setBooksLoading(false);
        setBooksLoaded(false);
        setDisplayChangeSerieStatusPopup(false);
        if (serieToDisplay.bookIds) {
            setPaddedVolumes(serieToDisplay.bookIds.length.toString().padStart(2, '0'))
        }
    }, [serieToDisplay]);


    return <>

        <div className={when(displayBooks).css("serie-block-display-books", "serie-block")}>
            <div className="serie-description-wrapper" >
                <div className='center-align' >
                    <a href="#!" onClick={switchDisplayBooks}>
                        <img src={bookApi().resolveSeriePicture(serieToDisplay)} alt="" className={when(displayBooks).css("serie-cover-display-books", "serie-cover")}></img>
                    </a>
                </div>
                <div className='serie-textual-description'>
                    <div className="valign-wrapper"><h5><i className='material-icons'>book</i>{serieToDisplay.title}</h5></div>
                    <div className="serie-actions">
                        <a href="#!" className={when(serieToDisplay.completed).thenDisableElement("waves-effect waves-light btn-small")} onClick={addBookToSerie}>
                            <i className="material-icons right">playlist_add</i>
                            {`${paddedVolumes} tomes`}
                        </a>
                        <a href="#!" className={when(serieToDisplay.completed).css("green", "waves-effect waves-light btn-small red")}
                            onClick={displayChangeSerieStatusPopupForSerie}>
                            <i className="material-icons right">{serieToDisplay.completed ? "check_box" : "check_box_outline_blank"}</i>
                            {serieToDisplay.completed ? "Complétée" : "Manquants"}
                        </a>
                        <a href="#!" className="waves-effect waves-light btn-small red accent-4"
                            onClick={displayDeletePopupHandler}>
                            <i className="material-icons">delete</i>
                        </a>
                    </div>
                </div>
            </div>
            <div className={when(!displayBooks).css("serie-with-books-wrapper-hidden", "serie-with-books-wrapper center-align")}>
                {allBooksForSerie.displayList.map((aBook, index) => <BookForListRenderer key={index} oneBook={aBook}></BookForListRenderer>)}
            </div>

        </div>

        <SimplerYesNoModalPopupComponent id={`changeSerieCompletedStatus${serieToDisplay.index}`}
            title={`La série ${serieToDisplay.title} est-elle complétée?`}
            open={displayChangeSerieStatusPopup}
            onClose={changeSerieStatusPopupCloseHandler}
            okLabel="La série est complète"
            cancelLabel="Il manque des tomes à la série"
        ></SimplerYesNoModalPopupComponent>

        <SimplerYesNoModalPopupComponent id={`deleteSerie${serieToDisplay.index}`}
            title={`Supprimer la série ${serieToDisplay.title} ?`}
            open={displayDeletePopup}
            onClose={deleteSeriePopupCloseHandler}
            okLabel="Supprimer"
            cancelLabel="Nope Nope"
        ></SimplerYesNoModalPopupComponent>
    </>
}