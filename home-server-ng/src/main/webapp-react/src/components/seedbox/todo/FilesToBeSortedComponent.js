import React from 'react';
import { useEffect, useState } from 'react';
import { useDisplayList } from '../../../tools/displayList';
import seedboxApi from '../../../apiManagement/SeedboxApi';
import {fileIconResolver} from '../../filemanager/FileIconResolver';
import { when } from '../../../tools/when';
import enhance from '../../../tools/enhance';
import { todoSubEventReactor } from './TodoSubEventReactor';
import eventReactor from '../../../eventReactor/EventReactor';
import on from '../../../tools/on';

export default function FilesToBeSortedComponent() {


    const [completedTorrents, setCompletedTorrents] = useDisplayList();

    useEffect(() => {

        const loadCompletedTorrent = () => seedboxApi().getCompletedTorrents().then(response => setCompletedTorrents({ ...completedTorrents.update(response).updateItems(enhance().selectable()) }));


        const unsubscriveOnCompleted = todoSubEventReactor().onMoveRequestSuccessFull(() => loadCompletedTorrent());


        const unsubscribeSearchEvent = eventReactor().shortcuts().onSearchEvent(searchString => {
            setCompletedTorrents({...completedTorrents.updateFilter(on().stringContains(searchString, oneFile => oneFile.name))})
        });

        loadCompletedTorrent();

        return () => {
            unsubscriveOnCompleted();
            unsubscribeSearchEvent();
        }

    }, []);

    const switchItemSelection = (oneCompletedTorrent) => {

        const newCompletedTorrents = completedTorrents.updateSelectableItems(oneCompletedTorrent.id, !oneCompletedTorrent.selected);

        setCompletedTorrents({ ...newCompletedTorrents});

        todoSubEventReactor().fileSelectionChange(newCompletedTorrents.getSelectedItems());

    }


    return <ul className="collection">
        {completedTorrents.displayList.map((oneCompletedTorrent, index) => <li key={index} className="collection-item avatar valign-wrapper">
            <a href="#!" onClick={() => switchItemSelection(oneCompletedTorrent)} className="valign-wrapper full-width">
                <i className={when(oneCompletedTorrent.selected).css("green jello-horizontal", "material-icons circle")}>{fileIconResolver(oneCompletedTorrent)}</i>

                <span className="title">{oneCompletedTorrent.name}</span>
                <span className={when(!oneCompletedTorrent.new).thenHideElement("new badge")}></span>
            </a>
        </li>)}

    </ul>
}