import React from 'react';
import { useEffect, useState } from 'react';

import { administrationApi } from '../../apiManagement/AdministrationApi';

import toaster from '../commons/Toaster';
import DataGridComponent from '../commons/DataGridComponent';
import RadioButtonComponent from '../commons/RadioButtonComponent';
import MasonryContainerComponent from '../commons/MasonryContainerComponent';
import UniqueCollapsibleContainerComponent from '../commons/UniqueCollapsibleContainerComponent';

import eventReactor from '../../eventReactor/EventReactor';
import { FORCE_CLEAR_SEARCH_EVENT } from '../../eventReactor/EventIds';

import on from '../../tools/on';
import { useDisplayList } from '../../tools/displayList';

export default function PropertiesComponent() {


    const [allProperties, setAllProperties] = useDisplayList();
    const [usableFilters, setUsableFilters] = useState([]);
    const [selectedFilter, setSelectedFilter] = useState(undefined);
    const [searchString, setSearchString] = useState('');

    const noneFilter = {
        moduleId: 'Aucun',
        enabled: true
    }

    const saveProperties = (allData, updatedRows) => {
        administrationApi()
            .updateProperties(updatedRows)
            .then(response => administrationApi().getAllProperties())
            .then(allPropertiesFromApi => {
                toaster().plopSuccess("propriétés sauvegardées avec succès");
                setAllProperties({ ...allProperties.update(allPropertiesFromApi) });
                setUsableFilters(buildFilterByModule(allPropertiesFromApi));
                eventReactor().emit(FORCE_CLEAR_SEARCH_EVENT);
            });
    }

    const buildFilterByModule = (properties) => {
        const filterSet = new Set(properties.map(oneProperty => oneProperty.moduleId));
        return [
            noneFilter,
            ...Array.from(filterSet)
                .map(oneFilterName => {
                    return {
                        moduleId: oneFilterName,
                        enabled: false
                    }
                }).sort((filter1, filter2) => filter1.moduleId.localeCompare(filter2.moduleId))];
    };

    const onSearchStringAndSelectedModule = (currentSearchString, currentFilter) => {
        return oneProperty => {

            let returnValue = true;

            if (currentFilter && currentFilter.moduleId !== noneFilter.moduleId) {
                returnValue = oneProperty.moduleId === currentFilter.moduleId;
            }

            return returnValue && on().stringContains(currentSearchString, oneModule => oneModule.id)(oneProperty);
        }
    }

    useEffect(() => {
        administrationApi().getAllProperties()
            .then(allPropertiesFromApi => {
                setAllProperties({ ...allProperties.update(allPropertiesFromApi) });
                setUsableFilters(buildFilterByModule(allPropertiesFromApi));
            })
    }, []);

    useEffect(() => {
        return eventReactor().shortcuts().onSearchEvent((data) => {
            setSearchString(data);
            setAllProperties({
                ...allProperties.updateFilter(onSearchStringAndSelectedModule(data, selectedFilter))
            });
        });
    }, [allProperties, selectedFilter]);


    const updateFilter = (filterToUpdate) => {
        const newFilterList = usableFilters
            .map(oneFilter => {
                return {
                    ...oneFilter,
                    enabled: oneFilter.moduleId === filterToUpdate.moduleId
                }
            });
        setUsableFilters(newFilterList);
        setSelectedFilter(filterToUpdate);
        setAllProperties({
            ...allProperties.updateFilter(onSearchStringAndSelectedModule(searchString, filterToUpdate))
        });
    }


    const dataGridConfiguration = {
        itemUniqueId: 'id',
        onSaveHandler: saveProperties,
        columns: [
            {
                header: 'Id',
                dataField: 'id',
                readOnlyFieldCondition: 'readOnly',
                defaultSort: true,
                onClickEditField: 'value'
            },
            {
                header: 'Valeur',
                dataField: 'value',
                renderer: 'textInput',
                hideOnSmallDevice: true
            },
            {
                header: 'Description',
                dataField: 'description',
                hideOnSmallDevice: true
            }
        ]
    };


    const filterPanelTitle = selectedFilter !== undefined ? ` (${selectedFilter.moduleId})` : '';

    return (
        <>
            <UniqueCollapsibleContainerComponent title={`Filtrer${filterPanelTitle}`}>
                <MasonryContainerComponent>
                    {usableFilters.map((oneFilter, filterIndex) => (
                        <div className="masonry-item" key={filterIndex}>
                            <RadioButtonComponent
                                text={oneFilter.moduleId}
                                dataswitch={oneFilter.enabled}
                                onChange={newValue => updateFilter(oneFilter)}
                            >
                            </RadioButtonComponent></div>
                    )
                    )}
                </MasonryContainerComponent>
            </UniqueCollapsibleContainerComponent>
            <DataGridComponent configuration={dataGridConfiguration} data={allProperties.displayList}></DataGridComponent>
        </>
    );
}