import React from 'react';
import { useEffect, useState } from 'react';

import { administrationApi } from '../../apiManagement/AdministrationApi';

import DataGridComponent from '../commons/DataGridComponent';

import eventReactor from '../../eventReactor/EventReactor';
import { SEARCH_EVENT, DISPLAYABLE_MODULES_CHANGED } from '../../eventReactor/EventIds';



export default function PluginsComponent() {


    const [allModules, setAllModules] = useState([]);
    const [filteredModules, setFilteredModules] = useState([]);


    useEffect(() => {
        setFilteredModules([...allModules]);
        administrationApi().getAllModule()
            .then(allModulesFromApi => {
                setAllModules([...allModulesFromApi]);
                setFilteredModules([...allModulesFromApi]);
            })
    }, []);

    useEffect(() => {
        return eventReactor().subscribe(SEARCH_EVENT, (data) => setFilteredModules(
            allModules.filter(oneModule =>
                oneModule.moduleId.toLowerCase().indexOf(data.toLowerCase()) !== -1
            ))
        );
    }, [allModules]);

    const saveNewModuleStatus = (allData, updatedRows) => {
        administrationApi()
            .updateModulesStatus(updatedRows)
            .then(allModulesFromApi => {
                setAllModules([...allModulesFromApi]);
                setFilteredModules([...allModulesFromApi]);

                eventReactor().emit(
                    DISPLAYABLE_MODULES_CHANGED,
                    allModulesFromApi.filter(oneModule => oneModule.hasClientSide && oneModule.enabled));
            });
    }

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
                <DataGridComponent configuration={dataGridConfiguration} data={filteredModules}></DataGridComponent>
            </div>
        </>
    );

}