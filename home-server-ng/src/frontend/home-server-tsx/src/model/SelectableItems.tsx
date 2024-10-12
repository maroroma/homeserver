import SelectableItem from "./SelectableItem";

export default class SelectableItems<T> {

    static empty<U>(): SelectableItems<U> {
        return new SelectableItems([]);
    }

    static of<U>(items: U[]): SelectableItems<U> {
        return new SelectableItems(items.map(anItem => new SelectableItem(anItem, false)));
    }

    constructor(public items: SelectableItem<T>[]) {
        this.select = this.select.bind(this);
        this.unselect = this.unselect.bind(this);
        this.switch = this.switch.bind(this);
        this.selectAll = this.selectAll.bind(this);
        this.unselectAll = this.unselectAll.bind(this);
        this.allSelected = this.allSelected.bind(this);
        this.noneSelected = this.noneSelected.bind(this);
        this.someSelected = this.someSelected.bind(this);
        this.nbSelected = this.nbSelected.bind(this);
        this.selectedItems = this.selectedItems.bind(this);
        this.filter = this.filter.bind(this);
    }

    select<U>(itemToAddToSelection: SelectableItem<T>, keySelector: (item: T) => U): SelectableItems<T> {
        return new SelectableItems(
            this.items.map(aSelectableItem => {
                if (keySelector(aSelectableItem.item) === keySelector(itemToAddToSelection.item)) {
                    return aSelectableItem.select();
                } else {
                    return aSelectableItem;
                }
            })
        )
    }

    unselect<U>(itemToAddToSelection: SelectableItem<T>, keySelector: (item: T) => U): SelectableItems<T> {
        return new SelectableItems(
            this.items.map(aSelectableItem => {
                if (keySelector(aSelectableItem.item) === keySelector(itemToAddToSelection.item)) {
                    return aSelectableItem.unselect();
                } else {
                    return aSelectableItem;
                }
            })
        )
    }

    switch<U>(itemToAddToSelection: SelectableItem<T>, keySelector: (item: T) => U): SelectableItems<T> {
        return new SelectableItems(
            this.items.map(aSelectableItem => {
                if (keySelector(aSelectableItem.item) === keySelector(itemToAddToSelection.item)) {
                    return aSelectableItem.switch();
                } else {
                    return aSelectableItem;
                }
            })
        )
    }

    selectAll(): SelectableItems<T> {
        return new SelectableItems(this.items.map(aSelectableItem => aSelectableItem.select()));
    }

    unselectAll(): SelectableItems<T> {
        return new SelectableItems(this.items.map(aSelectableItem => aSelectableItem.unselect()));
    }

    allSelected(): boolean {
        return this.items.every(aSelectableItem => aSelectableItem.selected);
    }

    noneSelected(): boolean {
        return !this.items.some(aSelectableItem => aSelectableItem.selected);
    }

    someSelected(): boolean {
        return this.items.some(aSelectableItem => aSelectableItem.selected);
    }

    nbSelected(): number {
        return this.items.filter(aSelectableItem => aSelectableItem.selected).length
    }

    selectedItems(): T[] {
        return this.items.filter(anItem => anItem.selected).map(aSelectedItem => aSelectedItem.item);
    }

    filter(predicate: (item: T) => boolean): SelectableItems<T> {
        return new SelectableItems(this.items.filter(aSelectableItem => predicate(aSelectableItem.item)));
    }

}