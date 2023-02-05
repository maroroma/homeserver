import React, { useEffect, useRef, useState } from 'react';
import bookApi from '../../../apiManagement/BookApi';
import { useDisplayList } from '../../../tools/displayList';
import enhance from '../../../tools/enhance';
import on from '../../../tools/on';
import sort from '../../../tools/sort';
import { when } from '../../../tools/when';
import { addBookSubReactor } from './AddBookSubReactor';


export default function SearchAndSelectSerieForBookComponent() {


    const [allSeriesList, setAllSeriesList] = useDisplayList();

    const [newSerieName, setNewSerieName] = useState("");
    const [searchSerieName, setSearchSerieName] = useState("");

    const updateNewSerieName = (event) => {
        setNewSerieName(event.target.value);
    }

    const updateSearchSerieName = (event) => {
        setSearchSerieName(event.target.value);
        setAllSeriesList({
            ...allSeriesList.updateFilter(
                on()
                    .stringContains(event.target.value, oneSerie => oneSerie.title))
        });
    }

    const addNewSerie = () => {
        bookApi().addNewSerie(newSerieName)
            .then(result => {
                setAllSeriesList({
                    ...allSeriesList
                        .update(result)
                        .updateItems(enhance().selectable())
                        .updateSelectableItems(newSerieName, true, oneSerie => oneSerie.title)
                });
                setNewSerieName("");

                addBookSubReactor().serieSelected(allSeriesList.getFirstSelectedItem());

            });
    }


    useEffect(() => {
        bookApi().getAllSeries()
            .then(result => {
                setAllSeriesList({ ...allSeriesList
                    .update(result)
                    .updateItems(enhance().selectable())
                    .updateSort(sort().basic(oneSerie => oneSerie.title))
                 });
            });

    }, []);

    const switchItemSelection = (oneSelectedSerie, event) => {
        event.stopPropagation();
        setAllSeriesList({
            ...allSeriesList
                .updateAllSelectableItems(false)
                .updateSelectableItems(oneSelectedSerie.id, !oneSelectedSerie.selected)
        });

        addBookSubReactor().serieSelected(allSeriesList.getFirstSelectedItem());
    }

    const skipSerieSelection = (event) => {
        event.stopPropagation();
        setAllSeriesList({
            ...allSeriesList
                .updateAllSelectableItems(false)
        });
        addBookSubReactor().skipSerie();
    };

    return <div>
        <a className="waves-effect waves-light btn" onClick={(event) => skipSerieSelection(event)}><i className="material-icons left">skip_next</i>Pas de série</a>

        <div className="input-field col s9">
            <i className="material-icons prefix small">book</i>
            <input id="searchExistingSerieInput" type="text" className="validate" value={searchSerieName} onChange={updateSearchSerieName}></input>
            <label htmlFor="searchExistingSerieInput">Nom de la série</label>
        </div>
        <ul className="collection with-header">
            {allSeriesList.displayList.map((oneSerie, index) => <li key={index} className="collection-item avatar valign-wrapper">
                <a href="#!" onClick={(event) => switchItemSelection(oneSerie, event)} className="valign-wrapper full-width">
                    <i className={when(oneSerie.selected).css("green jello-horizontal", "material-icons circle")}>book</i>

                    <span className="title">{oneSerie.title}</span>

                </a>
            </li>)}
        </ul>
        <div className="row  valign-wrapper">
            <div className="input-field col s9">
                <input id="addSerieInput" type="text" className="validate" value={newSerieName} onChange={updateNewSerieName}></input>
                <label htmlFor="addSerieInput">Nouvelle Série</label>
            </div>
            <div className="col s3">
                <a className={when(newSerieName === "").thenDisableElement("waves-effect waves-light btn-small")} href="#" onClick={addNewSerie}><i className="material-icons">add</i></a>
            </div>
        </div>
    </div>;
}