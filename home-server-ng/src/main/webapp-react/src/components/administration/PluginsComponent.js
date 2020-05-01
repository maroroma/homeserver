import React from 'react';
import { useEffect, useState } from 'react';

import { administrationApi } from '../../apiManagement/AdministrationApi';

import DataGridComponent from '../commons/DataGridComponent';

import eventReactor from '../../eventReactor/EventReactor';
import { SEARCH_EVENT, DISPLAYABLE_MODULES_CHANGED, FORCE_CLEAR_SEARCH_EVENT } from '../../eventReactor/EventIds';

import on from '../../tools/on';
import { useDisplayList } from '../../tools/displayList';


export default function PluginsComponent() {

    const [allModules, setAllModules] = useDisplayList();


    useEffect(() => {
        administrationApi().getAllModule()
            .then(allModulesFromApi => setAllModules({ ...allModules.update(allModulesFromApi) }))
    }, []);

    useEffect(() => {
        return eventReactor()
            .subscribe(SEARCH_EVENT, (data) => setAllModules(
                {
                    ...allModules
                        .updateFilter(on().stringContains(data, oneModule => oneModule.moduleId))
                }
            ));
    }, [allModules]);

    const saveNewModuleStatus = (allData, updatedRows) => {
        administrationApi()
            .updateModulesStatus(updatedRows)
            .then(allModulesFromApi => {
                setAllModules({ ...allModules.update(allModulesFromApi) })

                eventReactor().emit(FORCE_CLEAR_SEARCH_EVENT);

                eventReactor().emit(
                    DISPLAYABLE_MODULES_CHANGED,
                    allModulesFromApi.filter(oneModule => oneModule.hasClientSide && oneModule.enabled));
            });
    };

    const dataGridConfiguration = {
        itemUniqueId: 'moduleId',
        onSaveHandler: saveNewModuleStatus,
        columns: [
            {
                header: 'Actif',
                dataField: 'enabled',
                renderer: 'switch',
                readOnlyFieldCondition: 'readOnly'
            },
            {
                header: 'Module',
                dataField: 'moduleId',
                defaultSort: true
            },
            {
                header: 'Description',
                dataField: 'moduleDescription',
                hideOnSmallDevice: true
            }
        ]
    };

    return (
        <>
            <div>
                <DataGridComponent configuration={dataGridConfiguration} data={allModules.displayList}></DataGridComponent>
            </div>
        </>
    );

}