import React, {useEffect, useState} from 'react';
import bookApi from '../../apiManagement/BookApi';
import {emptyDisplayList, useDisplayList} from '../../tools/displayList';
import on from '../../tools/on';
import sort from '../../tools/sort';
import {when} from '../../tools/when';
import {ActionMenuComponent} from '../commons/ActionMenuComponent';
import modulesAdapter from '../mainmenu/ModulesAdapter';
import mofRouterEventReactor from '../mainmenu/MOFRouterEventReactor';
import {searchSubReactor} from '../mainmenu/SearchBarComponent';
import {EditBookComponent, editBookComponentEventReactor} from './EditBookComponent';
import SimpleBookRendererComponent from './SimpleBookRendererComponent';

import './AllBooksComponent.scss';
import SerieWithItsBooksRendererComponent from './SerieWithItsBooksRendererComponent';
import {SendMailPopupComponent, sendMailWithMissingBooksEventReactor} from './SendMailPopupComponent';


export default function AllBooksComponent() {


    // affichage standard
    const [allBooks, setAllBooks] = useDisplayList();

    // affichage avec groupement par série
    const [allBooksGroupedBySeries, setAllBooksGroupedBySeries] = useDisplayList();
    const [booksWithoutSerie, setBooksWithoutSerie] = useDisplayList();

    // pilote comment la liste de livre est affichée (groupement, tri, filtre)
    const [displaySettings, setDisplaySettings] = useState({
        mode: "SERIES_MODE"
    });

    const selectBookDisplayMode = () => setDisplaySettings({ ...displaySettings, mode: "BOOKS_MODE" });
    const selectSerieDisplayMode = () => setDisplaySettings({ ...displaySettings, mode: "SERIES_MODE" });


    const reloadAllBooks = (apiResult) => {

        // on vire la recherche en cours
        searchSubReactor().clearSearchBar();


        // on clean l'autre liste histoire de pas craquer la mémoire
        setAllBooksGroupedBySeries(emptyDisplayList());

        setAllBooks({
            ...allBooks
                .update(apiResult)
                .updateSort(sort().basic(bookFieldForSortExtractor))
        });

    };

    const reloadAllBooksGroupedBySeries = (apiResult) => {
        // on vire la recherche en cours
        searchSubReactor().clearSearchBar();


        // on clean l'autre liste histoire de pas craquer la mémoire
        setAllBooks(emptyDisplayList());

        setAllBooksGroupedBySeries({
            ...allBooksGroupedBySeries
                .update(
                    apiResult.booksWithSerie
                        .map(oneFullSerie => {
                            return { ...oneFullSerie, books: oneFullSerie.books.sort(sort().basic(bookFieldForSortExtractor)) }
                        }))
                .updateSort(sort().basic(oneFullSerie => oneFullSerie.title))
        });

        setBooksWithoutSerie({
            ...booksWithoutSerie.update(apiResult.booksWithoutSerie.books).updateSort(sort().basic(bookFieldForSortExtractor))
        });

    };


    useEffect(() => {
        if (displaySettings.mode === "BOOKS_MODE") {
            bookApi().getAllBooks().then(reloadAllBooks);

            const unsubscriveSearchForBookList = searchSubReactor().onSearchEvent(searchedString => {
                setAllBooks({ ...allBooks.updateFilter(on().stringContainsAllOf(searchedString.split(" "), extractorForSearchOnBook)) })
            });

            const unsubscriveSaveBook = editBookComponentEventReactor().onSaveBook(bookToSave => {
                bookApi().saveBook(bookToSave)
                    .then(result => editBookComponentEventReactor().cancel())
                    .then(result => bookApi().getAllBooks())
                    .then(reloadAllBooks);
            });

            const unsubscribeDeleteBook = editBookComponentEventReactor().onDeleteBook(bookToSave => {
                bookApi().deleteBook(bookToSave)
                    .then(result => editBookComponentEventReactor().cancel())
                    .then(result => bookApi().getAllBooks())
                    .then(reloadAllBooks);
            });

            return () => {
                unsubscriveSearchForBookList();
                unsubscriveSaveBook();
                unsubscribeDeleteBook();
            };



        } else {
            bookApi().getAllBooksGroupedBySeries().then(reloadAllBooksGroupedBySeries);

            const unsubscriveSearchForBookGroupedBySeriesList = searchSubReactor().onSearchEvent(searchedString => {
                // gestion sur les séries
                setAllBooksGroupedBySeries({ ...allBooksGroupedBySeries.updateFilter(on().stringContainsAllOf(searchedString.split(" "), oneSerie => oneSerie.title)) });

                // gestion pour les livres sans série
                console.log("booksWithoutSerie", booksWithoutSerie);
                setBooksWithoutSerie({
                    ...booksWithoutSerie.updateFilter(on().stringContainsAllOf(searchedString.split(" "), extractorForSearchOnBook))
                }
                );
            });

            const unsubscriveSaveBook = editBookComponentEventReactor().onSaveBook(bookToSave => {
                bookApi().saveBook(bookToSave)
                    .then(result => editBookComponentEventReactor().cancel())
                    .then(result => bookApi().getAllBooksGroupedBySeries())
                    .then(reloadAllBooksGroupedBySeries);
            });

            const unsubscribeDeleteBook = editBookComponentEventReactor().onDeleteBook(bookToSave => {
                bookApi().deleteBook(bookToSave)
                .then(result => editBookComponentEventReactor().cancel())
                .then(result => bookApi().getAllBooksGroupedBySeries())
                .then(reloadAllBooksGroupedBySeries);
            });

            return () => {
                unsubscriveSearchForBookGroupedBySeriesList();
                unsubscriveSaveBook();
                unsubscribeDeleteBook();
            };

        }


    }, [displaySettings]);

    useEffect(() => {
        // const unsubscriveSaveBook = editBookComponentEventReactor().onSaveBook(bookToSave => {
        //     bookApi().saveBook(bookToSave)
        //         .then(result => editBookComponentEventReactor().cancel())
        //         .then(result => bookApi().getAllBooks())
        //         .then(reloadAllBooks);
        // });

        // return () => {
        //     unsubscriveSaveBook();
        // }

    }, [allBooks]);

    // affichage de la page d'ajout
    const goToAddNewBook = () => {
        mofRouterEventReactor().selectedModuleChange(modulesAdapter().getMenuDescriptorForPath("/books/search"));
    }

    const openPopupForEditingOneBook = (oneBookToEdit) => {
        editBookComponentEventReactor().openEditPopup(oneBookToEdit);
    }

    // utilisé pour la construction des fonctions de tri sur les livres
    // todo : à sortir dans un helper
    const bookFieldForSortExtractor = (oneBook) => `${oneBook.title}${oneBook.subtitle}`;

    const extractorForSearchOnBook = (oneBook) => {
        let keyToReturn = `${oneBook.title} ${oneBook.subtitle} ${oneBook.author}`;
        if (oneBook.serieInfo !== undefined && oneBook.serieInfo !== null) {
            keyToReturn = `${keyToReturn} ${oneBook.serieInfo.serieName}`;
            keyToReturn = `${keyToReturn} ${oneBook.serieInfo.orderInSerie}`;
        }
        return keyToReturn;
    }


    return <div>
        <ul className={when(displaySettings.mode === "SERIES_MODE").thenHideElement("collection with-header")}>
            <li className="collection-header">
                <h4>{allBooks.rawList.length} livres dans la collection</h4>
            </li>
            {allBooks.displayList.map((oneBookResult, index) =>
                <SimpleBookRendererComponent
                    bookToDisplay={oneBookResult}
                    key={index}
                    useInitialImageLink={false}
                    onClick={() => openPopupForEditingOneBook(oneBookResult)}
                ></SimpleBookRendererComponent>)}
        </ul>

        <div className="books-by-serie">
            <ul className={when(displaySettings.mode === "BOOKS_MODE").thenHideElement("collection with-header")}>
                <li className="collection-header">
                    <h4>{allBooksGroupedBySeries.rawList.length} séries dans la collection</h4>
                </li>

                {allBooksGroupedBySeries.displayList.map((oneSerieWithBooks, index) => <SerieWithItsBooksRendererComponent
                    key={index}
                    oneSerieWithBooks={oneSerieWithBooks}
                    onBookClick={openPopupForEditingOneBook}>
                </SerieWithItsBooksRendererComponent>)}

                <li className="collection-header">
                    <h4>{booksWithoutSerie.rawList.length} livres sans série</h4>
                    {booksWithoutSerie.displayList.map((oneBookResult, index) =>
                        <SimpleBookRendererComponent
                            bookToDisplay={oneBookResult}
                            key={index}
                            useInitialImageLink={false}
                            onClick={() => openPopupForEditingOneBook(oneBookResult)}
                            otherProperty={"book-into-serie"}
                        ></SimpleBookRendererComponent>)}
                </li>
            </ul>
        </div>




        <ActionMenuComponent>
            <li><a href="#!" className="btn-floating btn-small green" onClick={goToAddNewBook} title="Ajouter un livre"><i className="material-icons">add</i></a></li>
            <li>
                <a href="#!" className={when(displaySettings.mode === "BOOKS_MODE").thenHideElement("btn-floating btn-small blue")}
                    title="Afficher par livres" onClick={selectBookDisplayMode}>
                    <i className="material-icons">library_books</i>
                </a>
            </li>
            <li>
                <a href="#!" className={when(displaySettings.mode !== "BOOKS_MODE").thenHideElement("btn-floating btn-small blue")}
                    title="Afficher par série" onClick={selectSerieDisplayMode}>
                    <i className="material-icons">view_column</i>
                </a>
            </li>
            <li><a href="#!" className="btn-floating btn-small blue" title="Recharger"><i className="material-icons">sync</i></a></li>
            <li><a href="#!" className="btn-floating btn-small orange" title="Envoyer les livres manquants" onClick={() => sendMailWithMissingBooksEventReactor().openSendMailPopup()}><i className="material-icons">send</i></a></li>
        </ActionMenuComponent>

        <EditBookComponent></EditBookComponent>
        <SendMailPopupComponent></SendMailPopupComponent>

        <div>

        </div>
    </div>
}