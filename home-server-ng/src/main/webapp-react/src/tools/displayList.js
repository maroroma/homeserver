export default class DisplayList {
    
    constructor(rawList) {
        this.rawList = rawList;
        this.displayList = [...rawList];
        this.clearFilters = this.clearFilters.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
        this.update = this.update.bind(this);
    }

    update(newRawList) {
        this.rawList = [...newRawList];
        return this.clearFilters();
    }

    clearFilters() {
        this.displayList = [...this.rawList];
        return this;
    }

    applyFilter(filter) {
        this.displayList = filter(this.rawList);
        return this;
    }
}