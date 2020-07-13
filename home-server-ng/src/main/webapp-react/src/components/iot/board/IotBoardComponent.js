import React from 'react';
import { useEffect, useState } from 'react';
import { useDisplayList } from '../../../tools/displayList';
import iotApi from '../../../apiManagement/IotApi';
import on from '../../../tools/on';
import sort from '../../../tools/sort';
import MasonryContainerComponent from '../../commons/MasonryContainerComponent';
import IotComponentRenderer from './IotComponentRenderer';
import iotSubEventReactor from '../iotSubEventReactor';
import eventReactor from '../../../eventReactor/EventReactor';


export default function IotBoardComponent() {


    const [allIotComponents, setAllIotComponents] = useDisplayList();

    useEffect(() => {

        iotApi().getAllIotComponents().then(response => setAllIotComponents({
            ...allIotComponents.update(response).updateSort(sort().basic(oneComponent => oneComponent.componentDescriptor.name))
        }))


        const unsubscribeSendActionRequest = iotSubEventReactor().onSendActionRequest(iotComponent => {
            console.log(iotComponent)

        });

        const unsubscribeSearch = eventReactor().shortcuts().onSearchEvent(searchString => setAllIotComponents({
            ...allIotComponents.updateFilter(on().stringContains(searchString, oneComponent => oneComponent.componentDescriptor.name))
        }));


        return () => {
            unsubscribeSendActionRequest();
            unsubscribeSearch();
        }



    }, []);


    return <MasonryContainerComponent>
        {allIotComponents.displayList.map((oneComponent, index) => <IotComponentRenderer key={index} iotComponent={oneComponent}></IotComponentRenderer>)}
    </MasonryContainerComponent>
}