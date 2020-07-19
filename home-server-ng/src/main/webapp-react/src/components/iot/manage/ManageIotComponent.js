import React from 'react';
import { useEffect, useState } from 'react';
import { useDisplayList } from '../../../tools/displayList';
import iotApi from '../../../apiManagement/IotApi';
import toaster from '../../commons/Toaster';
import sort from '../../../tools/sort';
import eventReactor from '../../../eventReactor/EventReactor';
import on from '../../../tools/on';
import DataGridComponent from '../../commons/DataGridComponent';
import {iotResolver} from '../IotRendererResolver';

export default function ManageIotComponent() {


    const [allIotComponents, setAllIotComponents] = useDisplayList();

    const flattenIotComponent = oneIotComponent => {
        return {
            ...oneIotComponent.componentDescriptor,
            available: oneIotComponent.available
        };
    };

    const unflattentIotComponent = oneComponentDescriptor => {
        return {
            componentDescriptor: oneComponentDescriptor
        };
    }

    const saveIotComponent = (allData, updatedRows) => {
        toaster().plopAndWait(() => iotApi().updateIotComponents(updatedRows.map(unflattentIotComponent)), "Sauvegarde en cours")
            .then(response => iotApi().getAllIotComponents())
            .then(response => response.map(flattenIotComponent))
            .then(response => setAllIotComponents({ ...allIotComponents.update(response) }));
    };

    const dataGridConfiguration = {
        itemUniqueId: 'id',
        onSaveHandler: saveIotComponent,
        columns: [
            {
                header: 'Type',
                dataField: 'componentType',
                hideOnSmallDevice: false,
                renderer: 'custom',
                customRenderer: (row, type) => <i className="material-icons">{iotResolver().resolveRender(type).icon}</i>
            },
            {
                header: 'Id',
                dataField: 'id',
                readOnlyFieldCondition: 'readOnly',
                defaultSort: true,
                hideOnSmallDevice: true
            },
            {
                header: 'Nom',
                dataField: 'name',
                renderer: 'textInput',
                hideOnSmallDevice: false
            },
            {
                header: 'IP',
                dataField: 'ipAddress',
                readOnlyFieldCondition: 'readOnly',
                hideOnSmallDevice: true
            },
            {
                header: 'Dispo',
                dataField: 'available',
                readOnlyFieldCondition: 'readOnly',
                hideOnSmallDevice: true
            }
        ]
    };


    useEffect(() => {

        toaster().plopAndWait(() => iotApi().getAllIotComponents(), "Chargement des composants...")
            .then(response => response.map(flattenIotComponent))
            .then(response => setAllIotComponents({
                ...allIotComponents.update(response).updateSort(sort().basic(oneComponent => oneComponent.id))
            }));


        const unsubscribeSearch = eventReactor().shortcuts().onSearchEvent(searchString => setAllIotComponents({
            ...allIotComponents.updateFilter(on().stringContains(searchString, oneComponent => oneComponent.componentDescriptor.name))
        }));


        return () => {
            unsubscribeSearch();
        }



    }, []);

    return <DataGridComponent configuration={dataGridConfiguration} data={allIotComponents.displayList}></DataGridComponent>
}