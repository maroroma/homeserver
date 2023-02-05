import React, { useEffect, useRef, useState } from 'react';
import bookApi from '../../apiManagement/BookApi';
import { useDisplayList } from '../../tools/displayList';
import sort from '../../tools/sort';
import SimplerYesNoModalPopupComponent from '../commons/SimplerYesNoModalPopupComponent';
import SerieRendererComponent from './SerieRendererComponent';


export default function AllSeriesComponent() {


    const [allSeries, setAllSeries] = useDisplayList();

    const [displayChangeSerieStatusPopup, setDisplayChangeSerieStatusPopup] = useState(false);

    const [selectedSerie, setSelectedSerie] = useState({});


    useEffect(() => {
        bookApi().getAllSeries().then(result => setAllSeries({ ...allSeries.update(result).updateSort(sort().basic(oneSerie => oneSerie.title)) }));
    }, []);

    const changeSerieStatusPopupCloseHandler = (result) => {
        setDisplayChangeSerieStatusPopup(false);
        if (result !== selectedSerie.completed) {
            bookApi().saveSerie({ ...selectedSerie, completed: result })
                .then(result => setAllSeries({ ...allSeries.update(result).updateSort(sort().basic(oneSerie => oneSerie.title)) }))
        }
    }

    const displayChangeSerieStatusPopupForSerie = (selectedSerie) => {
        setSelectedSerie(selectedSerie);
        setDisplayChangeSerieStatusPopup(true);
    }



    return <div>
        <div className="collection with-header">
            <div class="collection-header"><h4>{allSeries.rawList.length} series dans la collection</h4></div>
            {allSeries.displayList.map((oneBookResult, index) =>
                <SerieRendererComponent serieToDisplay={oneBookResult} key={index} onClick={() => displayChangeSerieStatusPopupForSerie(oneBookResult)}></SerieRendererComponent>)}
        </div>

        <SimplerYesNoModalPopupComponent id={"changeSerieCompletedStatus"}
            title={`La série ${selectedSerie.title} est-elle complétée?`}
            open={displayChangeSerieStatusPopup}
            onClose={changeSerieStatusPopupCloseHandler}
            okLabel="La série est complète"
            cancelLabel="Il manque des tomes à la série"
        ></SimplerYesNoModalPopupComponent>
    </div>
}