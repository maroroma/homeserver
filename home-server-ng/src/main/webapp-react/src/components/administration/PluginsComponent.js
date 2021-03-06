import React from 'react';
import { useEffect } from 'react';

import { administrationApi } from '../../apiManagement/AdministrationApi';

import DataGridComponent from '../commons/DataGridComponent';

import eventReactor from '../../eventReactor/EventReactor';
import { DISPLAYABLE_MODULES_CHANGED } from '../../eventReactor/EventIds';

import on from '../../tools/on';
import { useDisplayList } from '../../tools/displayList';
import { searchSubReactor } from '../mainmenu/SearchBarComponent';


export default function PluginsComponent() {

    const [allModules, setAllModules] = useDisplayList();


    useEffect(() => {
        administrationApi().getAllModule()
            .then(allModulesFromApi => setAllModules({ ...allModules.update(allModulesFromApi) }))
    }, []);

    useEffect(() => {
        return searchSubReactor().onSearchEvent((data) => setAllModules(
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

                searchSubReactor().clearSearchBar();

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