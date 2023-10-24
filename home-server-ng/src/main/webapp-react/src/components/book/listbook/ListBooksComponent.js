import React, {useEffect} from 'react';
import {useDisplayList} from '../../../tools/displayList';
import bookApi from '../../../apiManagement/BookApi';
import ProgressiveSerieRenderer from './ProgressiveSerieRenderer';
import sort from '../../../tools/sort';
import {ActionMenuComponent} from '../../commons/ActionMenuComponent';
import mofRouterEventReactor from '../../mainmenu/MOFRouterEventReactor';
import modulesAdapter from '../../mainmenu/ModulesAdapter';
import {EditBookComponent, editBookComponentEventReactor} from '../EditBookComponent';
import {SendMailPopupComponent, sendMailWithMissingBooksEventReactor} from '../SendMailPopupComponent';
import enhance from '../../../tools/enhance';
import {searchSubReactor} from '../../mainmenu/SearchBarComponent';
import on from '../../../tools/on';

export default function ListBooksComponent() {


    const [allSeries, setAllSeries] = useDisplayList();


    // affichage de la page d'import
    const goToAddNewBook = () => {
        mofRouterEventReactor().selectedModuleChange(modulesAdapter().getMenuDescriptorForPath("/books/import"));
    }

    useEffect(() => {

        const unsubscribeSearchEvent = searchSubReactor().onSearchEvent(searchString => {
            setAllSeries({
                ...allSeries.updateFilter(on().stringContainsAllOf(searchString.split(" "), oneSerie => oneSerie.title))
            })
        });

        bookApi().getAllSeries()
            .then(result => {
                setAllSeries({
                    ...allSeries
                        .update(result)
                        .updateItems(enhance().indexed())
                        .updateSort(sort().basic(aSerie => aSerie.title))
                });
            });


        const unsubscriveSaveBook = editBookComponentEventReactor().onSaveBook(bookToSave => {
            bookApi().saveBook(bookToSave)
                .then(result => editBookComponentEventReactor().cancel())
                .then(result => bookApi().getAllSeries())
                .then(result => setAllSeries({ ...allSeries.update(result).updateItems(enhance().indexed()) }))
        });

        const unsubscribeDeleteBook = editBookComponentEventReactor().onDeleteBook(bookToSave => {
            bookApi().deleteBook(bookToSave)
                .then(result => editBookComponentEventReactor().cancel())
                .then(result => bookApi().getAllSeries())
                .then(result => setAllSeries({ ...allSeries.update(result).updateItems(enhance().indexed()) }))
        });

        return () => {
            unsubscriveSaveBook();
            unsubscribeDeleteBook();
            unsubscribeSearchEvent();
        }

    }, []);

    const saveSerie = (updatedSerieToSave) => {
        console.log("saveSerie")
        bookApi().saveSerie(updatedSerieToSave)
            .then(results => setAllSeries({ ...allSeries.update(results).updateItems(enhance().indexed()) }));
    }

    const deleteSerie = (serieToDelete) => {
        console.log("deleteSerie")
        bookApi().deleteSerie(serieToDelete)
            .then(results => setAllSeries({ ...allSeries.update(results).updateItems(enhance().indexed()) }));
    }



    return <>
        {allSeries.displayList
            .map((aSerie, index) => <ProgressiveSerieRenderer key={index} serieToDisplay={aSerie} onSave={saveSerie} onDelete={deleteSerie}></ProgressiveSerieRenderer>)
        }

        <ActionMenuComponent>
            <li>
                <a href="#!" className="btn-floating btn-small green" onClick={goToAddNewBook} title="Ajouter un livre">
                    <i className="material-icons">add</i>
                </a>
            </li>
            <li><a href="#!" className="btn-floating btn-small blue" title="Recharger"><i className="material-icons">sync</i></a></li>
            <li><a href="#!" className="btn-floating btn-small orange" title="Envoyer les livres manquants" onClick={() => sendMailWithMissingBooksEventReactor().openSendMailPopup()}><i className="material-icons">send</i></a></li>
        </ActionMenuComponent>

        <EditBookComponent></EditBookComponent>
        <SendMailPopupComponent></SendMailPopupComponent>
    </>



}