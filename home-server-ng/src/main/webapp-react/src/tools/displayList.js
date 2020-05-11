
import { useState } from 'react';
import on from './on';
import sort from './sort';

export function useDisplayList() {
    return useState(emptyDisplayList());
}

export function emptyDisplayList() {
    return new DisplayList([]);
}

export class DisplayList {

    constructor(rawList) {
        this.rawList = rawList ? rawList : [];
        this.displayList = [...this.rawList];

        this.clearFilters = this.clearFilters.bind(this);
        this.updateFilter = this.updateFilter.bind(this);
        this.update = this.update.bind(this);
        this.updateItems = this.updateItems.bind(this);

        this.innerApplyFilter = this.innerApplyFilter.bind(this);
        this.innerApplySort = this.innerApplySort.bind(this);
        this.innerUpdateDisplayList = this.innerUpdateDisplayList.bind(this);

        this.selectedItemsCount = this.selectedItemsCount.bind(this);
        this.hasSelectedItems = this.hasSelectedItems.bind(this);
        this.hasNoSelectedItems = this.hasNoSelectedItems.bind(this);
        this.applySelectionMemento = this.applySelectionMemento.bind(this);
        this.selectionMemento = this.selectionMemento.bind(this);
        this.updateAllSelectableItems = this.updateAllSelectableItems.bind(this);
        this.updateSelectableItems = this.updateSelectableItems.bind(this);
        this.getSelectedItems = this.getSelectedItems.bind(this);

        this.currentFilter = on().passthrough();
        this.currentSort = sort().neutral();
    }

    update(newRawList) {
        if (newRawList) { this.rawList = [...newRawList]; }
        return this.innerUpdateDisplayList();
    }

    updateItems(updateFunction) {
        this.rawList = this.rawList.map(updateFunction);
        return this.innerUpdateDisplayList();
    }

    updateSelectableItems(id, newStatus, idResolver = oneItem => oneItem.id) {
        return this.updateItems(oneItem => {
            return {
                ...oneItem,
                selected: id === idResolver(oneItem) ? newStatus : oneItem.selected
            }
        });
    }

    updateAllSelectableItems(newStatus) {
        return this.updateItems(oneItem => {
            oneItem.selected = newStatus
            return oneItem;
        });
    }

    selectedItemsCount() {
        return this.getSelectedItems().length;
    }

    hasSelectedItems() {
        return this.selectedItemsCount() > 0;
    }

    hasNoSelectedItems() {
        return !this.hasSelectedItems();
    }

    getSelectedItems() {
        return this.rawList.filter(on().selected());
    }

    selectionMemento(idResolver = oneItem => oneItem.id) {
        return this.rawList.map(oneItem => {
            return {
                idToRestore: idResolver(oneItem),
                selected: oneItem.selected
            }
        });
    }


    applySelectionMemento(previousSelectionStatus, idResolver = oneItem => oneItem.id) {
        return this.updateItems(oneItem => {
            const previousItem = previousSelectionStatus.find(onePreviousItem => idResolver(oneItem) === onePreviousItem.idToRestore);
            const previousStatus = previousItem ? previousItem.selected : false;
            return {
                ...oneItem,
                selected: previousStatus
            }
        });

    };


    clearFilters() {
        return this.updateFilter(on().passthrough());
    }


    updateFilter(filter) {
        this.currentFilter = filter;
        return this.innerUpdateDisplayList();
    }

    updateSort(sortFunction) {
        this.currentSort = sortFunction;
        return this.innerUpdateDisplayList();
    }


    innerUpdateDisplayList() {
        return this.innerApplyFilter().innerApplySort();
    }

    innerApplyFilter() {
        this.displayList = this.rawList.filter(this.currentFilter);
        return this;
    }

    innerApplySort() {
        this.displayList = this.displayList.sort(this.currentSort);
        return this;
    }
}