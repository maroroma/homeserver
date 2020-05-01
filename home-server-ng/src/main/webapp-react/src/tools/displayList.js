
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


        this.currentFilter = on().passthrough();
        this.currentSort = sort().neutral();
    }

    update(newRawList) {
        this.rawList = [...newRawList];
        return this.innerUpdateDisplayList();
    }

    updateItems(updateFunction) {
        this.rawList = this.rawList.map(updateFunction);
        return this.innerUpdateDisplayList();
    }


    clearFilters() {
        return this.updateFilter(oneRawList => oneRawList);
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