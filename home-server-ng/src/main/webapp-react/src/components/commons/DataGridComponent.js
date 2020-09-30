import React from 'react';
import SwitchComponent from './SwitchComponent';
import IconComponent from './IconComponent';
import { ModalPopupComponent } from './ModalPopupComponent';
import { useState, useEffect } from 'react';
import sort from '../../tools/sort';
import orElse from '../../tools/orElse';
import { when } from '../../tools/when';
import eventReactor from '../../eventReactor/EventReactor';

import "./DataGridComponent.scss";


export default function DataGridComponent({ configuration, data }) {

    const [actionButtonsConfiguration, setActionButtonsConfiguration] = useState({
        displaySaveButton: true,
        displayDeleteAllButton: false,
        displayRefreshButton: false
    });

    const [dataToDisplay, setDataToDisplay] = useState([]);

    const [sortConfiguration, setSortConfiguration] = useState({
        sort: false,
        sortDirection: 1,
        sortField: undefined,
        sortFunction: sort().neutral()
    });
    const [changedRows, setChangedRows] = useState([]);
    const [popupDriver, setPopupDriver] = useState({
        id: 'popupEditProperty',
        open: false,
        title: 'Editer une propriété',
        okLabel: 'Ok',
        cancelLabel: 'Annuler',
        data: {}
    });


    const createSortFunction = (newSortConfiguration) => {
        return (item1, item2) => {
            let item1Value = '' + item1[newSortConfiguration.sortField];
            let item2Value = '' + item2[newSortConfiguration.sortField];
            return item1Value.localeCompare(item2Value) * newSortConfiguration.sortDirection;
        };
    }

    useEffect(() => {


        const columWithDefaultSortConfiguration = configuration.columns
            .filter(oneColumnConfig => oneColumnConfig.defaultSort)
            .slice()[0];

        const sortField = columWithDefaultSortConfiguration?.dataField;
        const sortDirection = columWithDefaultSortConfiguration?.defaultSortInverted ? -1 : 1

        let sortFunction = sort().neutral();
        if (sortConfiguration.sort) {
            sortFunction = createSortFunction(sortConfiguration);
        }

        setSortConfiguration({
            ...sortConfiguration,
            sort: sortField !== undefined,
            sortField: sortField,
            sortDirection: sortDirection,
            sortFunction: sortFunction
        });

        setActionButtonsConfiguration({
            ...actionButtonsConfiguration,
            displaySaveButton: orElse(configuration.displaySaveButton, true),
            displayDeleteAllButton: orElse(configuration.displayDeleteAllButton, false),
            displayRefreshButton: orElse(configuration.displayRefreshButton, false)
        });

        setDataToDisplay(data.sort(sortFunction));

        setChangedRows([]);



    }, [data, configuration]);



    const updateRow = (rowToUpdate) => {
        setDataToDisplay(dataToDisplay
            .filter(oneRow => oneRow[configuration.itemUniqueId] !== rowToUpdate[configuration.itemUniqueId])
            .concat(rowToUpdate)
            .sort(sortConfiguration.sortFunction));

        setChangedRows([...changedRows, rowToUpdate]);
    }

    const updateSort = (columnConfiguration) => {
        const sortDirection = sortConfiguration.sortField === columnConfiguration.dataField ? sortConfiguration.sortDirection * -1 : 1;
        const newSortConfiguration = {
            ...sortConfiguration,
            sortField: columnConfiguration.dataField,
            sortDirection: sortDirection
        };
        const newSortFunction = createSortFunction(newSortConfiguration);
        setSortConfiguration({ ...newSortConfiguration, sortFunction: newSortFunction });
        setDataToDisplay(dataToDisplay.sort(newSortFunction));
    }


    const resolveColumnClass = (columnConfiguration) => {
        if (columnConfiguration.hideOnSmallDevice) {
            return 'hide-on-small-only'
        }

        return '';
    }
    const openEditPopup = (columnConfiguration, row) => {
        const columnToEdit = configuration.columns
            .filter(oneColumn => oneColumn.dataField === columnConfiguration.onClickEditField)[0];

        const updatePopupData = (currentPopupProvider, newData) => {
            setPopupDriver({
                ...currentPopupProvider,
                data: {
                    ...currentPopupProvider.data,
                    dataToEdit: newData
                }
            });
        }

        const applyDataModificationOnClose = (currentPopupProvider) => {
            row[currentPopupProvider.data.columnToEdit.dataField] = currentPopupProvider.data.dataToEdit;
            updateRow(row);
        }

        setPopupDriver({
            ...popupDriver,
            open: true,
            data: {
                columnToEdit: columnToEdit,
                dataToEdit: row[columnToEdit.dataField]
            },
            updateData: updatePopupData,
            onOk: applyDataModificationOnClose
        });
    };

    const buildCell = (columnConfiguration, row, rowIndex, columnIndex) => {

        // si bouton de suppression, rien
        if (columnConfiguration.isDeleteButton === true) {
            return <td key={`${rowIndex}_${columnIndex}`} onClick={() => eventReactor().shortcuts().dataGridDeleteOne(row)}>
                <button className="btn-floating btn-small red">
                    <IconComponent icon="delete"></IconComponent>
                </button>
            </td>
        }


        const dataForCell = row[columnConfiguration.dataField];
        const readOnly = columnConfiguration.readOnlyFieldCondition !== undefined ?
            row[columnConfiguration.readOnlyFieldCondition] : false;
        let cellContent = null;
        if (!columnConfiguration.renderer && !columnConfiguration.onClickEditField) {
            cellContent = (`${dataForCell}`);
        }

        if (!columnConfiguration.renderer && columnConfiguration.onClickEditField) {
            cellContent = (<div data-target="editPopup" onClick={() => openEditPopup(columnConfiguration, row)}>{`${dataForCell}`}</div>);
        }


        const updateData = (newData) => {
            row[columnConfiguration.dataField] = newData;
            updateRow(row);
        }

        if (columnConfiguration.renderer === 'switch') {
            cellContent = (<SwitchComponent dataswitch={dataForCell} onChange={updateData} disabled={readOnly}></SwitchComponent >);
        }
        if (columnConfiguration.renderer === 'textInput') {
            cellContent = (<div className="input-field">
                <input type="text" className="validate" value={dataForCell} onChange={(event) => updateData(event.target.value)} disabled={readOnly}></input>
            </div>);
        }
        if (columnConfiguration.renderer === 'custom') {
            cellContent = columnConfiguration.customRenderer(row, dataForCell);
        }

        const cellClassName = resolveColumnClass(columnConfiguration);

        return (<td key={`${rowIndex}_${columnIndex}`} className={cellClassName}>{cellContent}</td>);
    }

    const buildHeader = (oneColumnConfig, index) => {

        // si bouton de suppression, rien
        if (oneColumnConfig.isDeleteButton === true) {
            return <th key={index}></th>
        }


        const isSortField = sortConfiguration.sortField === oneColumnConfig.dataField;
        let sortArrow = null;
        if (isSortField) {
            if (sortConfiguration.sortDirection > 0) {
                sortArrow = (<IconComponent icon="arrow_drop_up" classAddons="tiny"></IconComponent>);
            } else {
                sortArrow = (<IconComponent icon="arrow_drop_down" classAddons="tiny"></IconComponent>);
            }
        }

        const cellClassName = resolveColumnClass(oneColumnConfig);

        return (<th key={index} onClick={() => updateSort(oneColumnConfig)} className={cellClassName}>{oneColumnConfig.header}{sortArrow}</th>)


    };

    const onSaveClickHandler = () => {
        if (configuration.onSaveHandler !== undefined) {
            configuration.onSaveHandler(dataToDisplay, changedRows);
        }
    }

    const onDeleteClickHandler = () => {
        eventReactor().shortcuts().dataGridDeleteAll();
    }

    const onRefreshClickHandler = () => {
        eventReactor().shortcuts().dataGridRefreshAll();
    }

    const btnSaveClass = changedRows.length > 0 ? 'blue pulse' : 'disabled';


    return (
        <>
            <table className="datagrid">
                <thead>
                    <tr>
                        {
                            configuration.columns.map(buildHeader)
                        }
                    </tr>
                </thead>
                <tbody>
                    {
                        dataToDisplay.map((oneRowOfData, rowIndex) => (
                            <tr key={rowIndex}>
                                {configuration.columns.map((oneColumnConfig, columnIndex) =>
                                    <React.Fragment key={`${rowIndex}_${columnIndex}`}>{buildCell(oneColumnConfig, oneRowOfData, rowIndex, columnIndex)}</React.Fragment>
                                )}
                            </tr>))
                    }
                </tbody>
            </table>


            <div className="fixed-action-btn">
                <a className={when(!actionButtonsConfiguration.displayRefreshButton).thenHideElement(`btn-floating btn-medium blue btn-with-siblings`)} onClick={onRefreshClickHandler}>
                    <IconComponent icon="sync" classAddons="large"></IconComponent>
                </a>
                <a className={when(!actionButtonsConfiguration.displaySaveButton).thenHideElement(`btn-floating btn-large ${btnSaveClass}`)} onClick={onSaveClickHandler}>
                    <IconComponent icon="save" classAddons="large"></IconComponent>
                </a>
                <a className={when(!actionButtonsConfiguration.displayDeleteAllButton).thenHideElement(`btn-floating btn-large red`)} onClick={onDeleteClickHandler}>
                    <IconComponent icon="delete" classAddons="large"></IconComponent>
                </a>
            </div>

            <ModalPopupComponent popupId="editPopup" driver={popupDriver}>
                <div className="input-field">
                    <input id="propertyToEdit" type="text" className="validate" value={popupDriver.data?.dataToEdit} onChange={(event) => popupDriver.updateData(popupDriver, event.target.value)}></input>
                </div>
            </ModalPopupComponent>

        </>
    );

}