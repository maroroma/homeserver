import React from 'react';
import { useEffect, useState } from 'react';

import { administrationApi } from '../../apiManagement/AdministrationApi';

import toaster from '../commons/Toaster';
import DataGridComponent from '../commons/DataGridComponent';
import RadioButtonComponent from '../commons/RadioButtonComponent';
import MasonryContainerComponent from '../commons/MasonryContainerComponent';
import CollapsibleContainerComponent from '../commons/CollapsibleContainerComponent';

import eventReactor from '../../eventReactor/EventReactor';
import { SEARCH_EVENT, FORCE_CLEAR_SEARCH_EVENT } from '../../eventReactor/EventIds';

import on from '../../tools/on';

export default function PropertiesComponent() {


    const [allProperties, setAllProperties] = useState([]);
    const [filteredProperties, setFilteredProperties] = useState([]);
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
                setAllProperties([...allPropertiesFromApi]);
                setFilteredProperties([...allPropertiesFromApi]);
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

    const calculateFilteredProperties = (currentSearchString, currentFilter) => {
        let returnValue = allProperties;

        if (currentFilter && currentFilter.moduleId !== noneFilter.moduleId) {
            returnValue = returnValue.filter(oneProperty => oneProperty.moduleId === currentFilter.moduleId);
        }

        returnValue = returnValue.filter(on().stringContains(currentSearchString, oneModule => oneModule.id));
        return returnValue;
    }

    useEffect(() => {
        setFilteredProperties([...allProperties]);
        administrationApi().getAllProperties()
            .then(allPropertiesFromApi => {
                setAllProperties([...allPropertiesFromApi]);
                setFilteredProperties([...allPropertiesFromApi]);
                setUsableFilters(buildFilterByModule(allPropertiesFromApi));
            })
    }, []);

    useEffect(() => {
        return eventReactor().subscribe(SEARCH_EVENT, (data) => {
            setSearchString(data);
            setFilteredProperties(
                calculateFilteredProperties(data, selectedFilter)
            )
        }
        );
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
        setFilteredProperties(calculateFilteredProperties(searchString, filterToUpdate));
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
            <CollapsibleContainerComponent title={`Filtrer${filterPanelTitle}`}>
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
            </CollapsibleContainerComponent>
            <DataGridComponent configuration={dataGridConfiguration} data={filteredProperties}></DataGridComponent>
        </>
    );
}