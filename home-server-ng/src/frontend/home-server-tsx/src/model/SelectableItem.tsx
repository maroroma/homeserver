export default class SelectableItem<T> {



    constructor(public item: T, public selected: boolean) { }

    map<U>(selectedValue: U, notSelectedValue: U): U {
        if (this.selected) {
            return selectedValue;
        } else {
            return notSelectedValue;
        }
    }

    select(): SelectableItem<T> {
        return new SelectableItem(this.item, true);
    }

    unselect(): SelectableItem<T> {
        return new SelectableItem(this.item, false);
    }

    switch(): SelectableItem<T> {
        if (this.selected) {
            return this.unselect();
        } else {
            return this.select();
        }
    }
}