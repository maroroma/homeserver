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
import { SELECT_ITEM } from '../../../eventReactor/EventIds';
import { usePopupDriver, ModalPopupComponent } from '../../commons/ModalPopupComponent';
import SpriteListComponent from '../sprites/SpriteListComponent';
import toaster from '../../commons/Toaster';


export default function IotBoardComponent() {


    const [allIotComponents, setAllIotComponents] = useDisplayList();
    const [selectedIotComponent, setSelectedIotComponent] = useState({});

    const [popupSelectSpriteDriver, setPopupSelectSpriteDriver] = usePopupDriver({
        id: "popupSelectSprite",
        title: "choisir un sprite",
        okLabel: "Annuler",
        noCancelButton: true
    })

    useEffect(() => {

        toaster().plopAndWait(() => iotApi().getAllIotComponents(), "Chargement des composants...")
            .then(response => setAllIotComponents({
                ...allIotComponents.update(response).updateSort(sort().basic(oneComponent => oneComponent.componentDescriptor.name))
            }));


        const unsubscribeSendActionRequest = iotSubEventReactor().onSendActionRequest(iotComponent => {
            setSelectedIotComponent(iotComponent);
            setPopupSelectSpriteDriver({ ...popupSelectSpriteDriver, open: true });
        });

        const unsubscribeSearch = eventReactor().shortcuts().onSearchEvent(searchString => setAllIotComponents({
            ...allIotComponents.updateFilter(on().stringContains(searchString, oneComponent => oneComponent.componentDescriptor.name))
        }));


        return () => {
            unsubscribeSendActionRequest();
            unsubscribeSearch();
        }



    }, []);

    useEffect(() => {
        const unsubscribeSelect = eventReactor().subscribe(SELECT_ITEM, data => {
            if (data.source === "SPRITE_SELECTION") {
                iotApi().sendBuzz(selectedIotComponent, data.itemId)
                    .then(() => setPopupSelectSpriteDriver({ ...popupSelectSpriteDriver, open: false }));
            }
        });

        return () => {
            unsubscribeSelect();
        }
    }, [selectedIotComponent]);


    return <div><MasonryContainerComponent>
        {allIotComponents.displayList.map((oneComponent, index) => <IotComponentRenderer key={index} iotComponent={oneComponent}></IotComponentRenderer>)}
    </MasonryContainerComponent>

        <ModalPopupComponent driver={popupSelectSpriteDriver}>
            <SpriteListComponent onlyDisplay={true}></SpriteListComponent>
        </ModalPopupComponent>

    </div>
}