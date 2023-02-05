import React, { useEffect, useRef, useState } from 'react';
import bookApi from '../../apiManagement/BookApi';
import eventReactor from '../../eventReactor/EventReactor';
import { useDisplayList } from '../../tools/displayList';
import orElse from '../../tools/orElse';
import sort from '../../tools/sort';
import { UniqueCollapsibleContainerComponent, uniqueCollapsibleContainerSubReactor } from '../commons/UniqueCollapsibleContainerComponent';
import SerieRendererComponent from './SerieRendererComponent';
import "./EditBookComponent.scss";
import enhance from '../../tools/enhance';
import { when } from '../../tools/when';
import SimplerYesNoModalPopupComponent from '../commons/SimplerYesNoModalPopupComponent';
import popup from '../../tools/popup';


export function editBookComponentEventReactor() {
    const cancel = () => eventReactor().emit("EDIT_BOOK_COMPONENT_CANCEL_CLOSE");
    const onCancel = (eventHandler) => eventReactor().subscribe("EDIT_BOOK_COMPONENT_CANCEL_CLOSE", eventHandler);

    const openEditPopup = (bookToEdit) => eventReactor().emit("EDIT_BOOK_COMPONENT_OPEN", bookToEdit);
    const onOpenEditPopup = (eventHandler) => eventReactor().subscribe("EDIT_BOOK_COMPONENT_OPEN", eventHandler);

    const saveBook = (bookToSave) => eventReactor().emit("EDIT_BOOK_SAVE_BOOK", bookToSave);
    const onSaveBook = (eventHandler) => eventReactor().subscribe("EDIT_BOOK_SAVE_BOOK", eventHandler);

    const deleteBook = (bookToSave) => eventReactor().emit("EDIT_BOOK_DELETE_BOOK", bookToSave);
    const onDeleteBook = (eventHandler) => eventReactor().subscribe("EDIT_BOOK_DELETE_BOOK", eventHandler);

    return {
        cancel: cancel,
        onCancel: onCancel,
        openEditPopup: openEditPopup,
        onOpenEditPopup: onOpenEditPopup,
        saveBook: saveBook,
        onSaveBook: onSaveBook,
        deleteBook: deleteBook,
        onDeleteBook: onDeleteBook
    }
}

export function EditBookComponent() {

    const [selfPopupInstance, setSelfPopupInstance] = useState(undefined);

    const [serieList, setSerieList] = useDisplayList();

    const [oneBookUnderEdition, setOneBookUnderEdition] = useState({});

    const [changesHappened, setChangesHappened] = useState(false);

    const [displayDeleteConfirmation, setDisplayConfirmation] = useState(false);

    // gestion de la dropdownlist
    useEffect(() => {
        var elems = document.querySelectorAll('select');
        var instances = window.M.FormSelect.init(elems, {
            direction: 'left',
            hoverEnabled: false
        });

        return () => {
            instances.forEach(oneInstance => oneInstance.destroy());
        };
    }, [oneBookUnderEdition, serieList]);


    // recup des liste, gestion popup
    useEffect(() => {

        // récupération des séries
        bookApi().getAllSeries().then(result => setSerieList({ ...serieList.update(result).updateSort(sort().basic(oneSerie => oneSerie.title)) }));

        // gestion de la popup
        const instance = popup("editBookPopupInstance", setSelfPopupInstance, innerCloseFromInstance);

        return () => {
            instance.destroy();
        }


    }, []);


    // gestion des events
    useEffect(() => {

        const unsubscribeOnOpenEditPopup = editBookComponentEventReactor().onOpenEditPopup(bookToEdit => {
            const cloneWithDefaultValue = {
                id: orElse(bookToEdit.id, ""),
                title: orElse(bookToEdit.title, ""),
                subtitle: orElse(bookToEdit.subtitle, ""),
                isbns: orElse(bookToEdit.isbns, []),
                author: orElse(bookToEdit.author, ""),
                owner: orElse(bookToEdit.owner, ""),
                pageCount: orElse(bookToEdit.pageCount, 0),
                initialImageLink: orElse(bookToEdit.initialImageLink, ""),
                pictureFileId: orElse(bookToEdit.pictureFileId, ""),
                serieInfo: orElse(bookToEdit.serieInfo, { serieName: "", serieId: "", orderInSerie: "" })
            };

            setOneBookUnderEdition({ ...cloneWithDefaultValue });
            setChangesHappened(false);

            if (selfPopupInstance) {
                selfPopupInstance.open();
            }
        });

        const unsubscribeCancel = editBookComponentEventReactor().onCancel(() => {
            if (selfPopupInstance) {
                selfPopupInstance.close();
            }
        });


        return () => {
            unsubscribeOnOpenEditPopup();
            unsubscribeCancel();
        };

    }, [selfPopupInstance]);


    const innerCloseFromInstance = () => console.log("voir ce que je fais de toi");


    const removeSerieFromBook = () => {
        setOneBookUnderEdition({ ...oneBookUnderEdition, serieInfo: { serieName: "", serieId: "", orderInSerie: "" } });
        setChangesHappened(true);
        uniqueCollapsibleContainerSubReactor().collapse("swithSerieForEditBookComponent");
    }


    const serieChanged = (event) => {
        setOneBookUnderEdition({ ...oneBookUnderEdition, serieInfo: { ...oneBookUnderEdition.serieInfo, serieName: event.title, serieId: event.id } });
        setChangesHappened(true);
        uniqueCollapsibleContainerSubReactor().collapse("swithSerieForEditBookComponent");
    }

    const propertyChanged = (propertyName, eventWithValue) => {
        oneBookUnderEdition[propertyName] = eventWithValue.target.value;
        setChangesHappened(true);
        setOneBookUnderEdition({ ...oneBookUnderEdition });
    }

    const orderInSerieChanged = (eventWithValue) => {
        if (oneBookUnderEdition.serieInfo === undefined || oneBookUnderEdition.serieInfo === null) {
            oneBookUnderEdition.serieInfo = { orderInSerie: eventWithValue.target.value };
        } else {
            oneBookUnderEdition.serieInfo = { ...oneBookUnderEdition.serieInfo, orderInSerie: eventWithValue.target.value };
        }
        setChangesHappened(true);
        setOneBookUnderEdition({ ...oneBookUnderEdition });
    }

    const confirmDeleteBookResponseHandler = (result) => {
        setDisplayConfirmation(false);
        if (result === true) {
            editBookComponentEventReactor().deleteBook(oneBookUnderEdition);
        }
    }


    return oneBookUnderEdition ?

        <div className="modal" id="editBookPopupInstance">
            <div className="modal-content">


                <div className="modal-header">
                    <img src={(bookApi().downloadBaseUrl() + "/" + oneBookUnderEdition.id + "/picture")} alt="" className="left"></img>
                    <h4 className="teal-text lighten-1">{`${oneBookUnderEdition.title} ${oneBookUnderEdition.subtitle}`}</h4>
                    <div className="right">
                        <a className={when(!changesHappened).thenDisableElement(`btn-floating btn-large blue action-button ${changesHappened ? "pulse" : ""}`)}
                            onClick={() => editBookComponentEventReactor().saveBook(oneBookUnderEdition)}>
                            <i className="large material-icons">save</i>
                        </a>
                        <a className="action-button btn-floating red darken-4" onClick={() => setDisplayConfirmation(true)}>
                            <i className="large material-icons">delete</i>
                        </a>
                        <a className="action-button btn-floating btn-large red" onClick={() => editBookComponentEventReactor().cancel()}>
                            <i className="large material-icons">undo</i>
                        </a>
                    </div>
                </div>
                <div>
                    <div className="input-field">
                        <input placeholder="Titre" id="book_title" type="text" className="validate" value={oneBookUnderEdition.title} onChange={event => propertyChanged("title", event)}></input>
                        <label htmlFor="book_title" className="active">Titre</label>
                    </div>
                    <div className="input-field">
                        <input placeholder="Sous Titre" id="book_subtitle" type="text" className="validate" value={oneBookUnderEdition.subtitle} onChange={event => propertyChanged("subtitle", event)}></input>
                        <label htmlFor="book_subtitle" className="active">Sous Titre</label>
                    </div>
                    <div className="input-field">
                        <input placeholder="Auteur" id="book_author" type="text" className="validate" value={oneBookUnderEdition.author} onChange={event => propertyChanged("author", event)}></input>
                        <label htmlFor="book_author" className="active">Auteur</label>
                    </div>
                    <div className="input-field">
                        <input placeholder="Ordre dans la série" id="order_in_serie" type="text" className="validate" value={oneBookUnderEdition.serieInfo?.orderInSerie} onChange={orderInSerieChanged}></input>
                        <label htmlFor="order_in_serie" className="active">Ordre dans la série</label>
                    </div>
                    <div className="input-field">
                        <input placeholder="A qui c'est ?" id="owner" type="text" className="validate" value={oneBookUnderEdition.owner} onChange={event => propertyChanged("owner", event)}></input>
                        <label htmlFor="owner" className="active">A qui c'est ?</label>
                    </div>
                    <div className="input-field">
                        <input placeholder="Série" id="book_serie" type="text" disabled className="validate" value={oneBookUnderEdition.serieInfo?.serieName}></input>
                        <label htmlFor="book_serie" className="active">Série</label>
                    </div>
                    <UniqueCollapsibleContainerComponent id="swithSerieForEditBookComponent" title="Changer la série">
                        <div className="collection with-header">
                            <a className="collection-item red lighten-4" onClick={removeSerieFromBook}>Détacher de la série
                                <div className="secondary-content"><i className="material-icons">delete</i></div>
                            </a>
                            {serieList.displayList.map((oneSerie, index) =>
                                <SerieRendererComponent serieToDisplay={oneSerie} key={`serie_renderer_book_edit_${index}`} onClick={event => serieChanged(oneSerie)}></SerieRendererComponent>)}
                        </div>
                    </UniqueCollapsibleContainerComponent>




                    <div className="input-field">
                        <input id="book_id" placeholder="Série" type="text" disabled className="validate" value={oneBookUnderEdition.id}></input>
                        <label htmlFor="book_id" className="active">Identifiant homeserver</label>
                    </div>
                    <div className="input-field">
                        <input id="book_isbn" placeholder="isbn" type="text" disabled className="validate" value={oneBookUnderEdition.isbns?.[0]}></input>
                        <label htmlFor="book_id" className="active">Isbns</label>
                        {oneBookUnderEdition.isbns?.filter((oneIsbn, index) => index > 0).map(oneIsbn => <input key={oneIsbn} placeholder="isbn" type="text" disabled className="validate" value={oneIsbn}></input>)}
                    </div>
                    <div className="input-field">

                    </div>
                </div>


                <div className="modal-footer">
                    <a className={when(!changesHappened).thenDisableElement(`btn-floating btn-large blue action-button ${changesHappened ? "pulse" : ""}`)}
                        onClick={() => editBookComponentEventReactor().saveBook(oneBookUnderEdition)}>
                        <i className="large material-icons">save</i>
                    </a>
                    <a className="action-button btn-floating red darken-4" onClick={() => setDisplayConfirmation(true)}>
                        <i className="large material-icons">delete</i>
                    </a>
                    <a className="action-button btn-floating btn-large red" onClick={() => editBookComponentEventReactor().cancel()}>
                        <i className="large material-icons">undo</i>
                    </a>
                </div>
            </div>
            <SimplerYesNoModalPopupComponent id={"yesNoForDeleteBook"}
                title={`Voulez-vous vraiment supprimer ${oneBookUnderEdition.title} - ${oneBookUnderEdition.subtitle} ?`}
                open={displayDeleteConfirmation}
                onClose={confirmDeleteBookResponseHandler}
                okLabel="Supprimer"
                cancelLabel="Finalement Non"
            ></SimplerYesNoModalPopupComponent>
        </div >





        : <></>;
}