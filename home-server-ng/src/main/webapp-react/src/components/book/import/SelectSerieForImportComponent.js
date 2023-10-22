import React, {useEffect, useState} from 'react';
import {when} from '../../../tools/when';
import {useImportBookContext} from './ImportBookContext';
import SerieRendererComponent from '../SerieRendererComponent';


export default function SelectSerieForImportComponent() {

    const { dispatchSerieUpdated, dispatchLoadAllSeries, allSeries, searchSeriesFilter, dispatchSearchSeries, dispatchAddNewSerie } = useImportBookContext();

    const [newSerieName, setNewSerieName] = useState("");

    const updateNewSerieName = (event) => {
        setNewSerieName(event.target.value);
    }

    const updateSearchSerieName = (event) => {
        dispatchSearchSeries(event.target.value);
    }

    const addNewSerie = () => {
        dispatchAddNewSerie(newSerieName);
        setNewSerieName("");
    }


    useEffect(() => {
        dispatchLoadAllSeries();
    }, []);

    const switchItemSelection = (oneSelectedSerie) => {
        dispatchSerieUpdated(oneSelectedSerie);
    }


    return <div>
        <div className="input-field col s9">
            <i className="material-icons prefix small">book</i>
            <input id="searchExistingSerieInput" type="text" className="validate" value={searchSeriesFilter} onChange={updateSearchSerieName}></input>
            <label htmlFor="searchExistingSerieInput">Nom de la série</label>
        </div>
        <div className="collection with-header">
            {allSeries.displayList.map((oneBookResult, index) =>
                <SerieRendererComponent serieToDisplay={oneBookResult} key={index} onClick={() => switchItemSelection(oneBookResult)}></SerieRendererComponent>)}
        </div>
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