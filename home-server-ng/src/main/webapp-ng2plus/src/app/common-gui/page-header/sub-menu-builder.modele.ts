import { SubMenuItem } from './sub-menu-item.modele';
export class SubMenuBuilder {

    public items = new Array<SubMenuItemBuilder>();

    public static item(label: string): SubMenuItemBuilder {
        return new SubMenuItemBuilder(label, new SubMenuBuilder());
    }

    public build(): Array<SubMenuItem> {
        const returnValue = new Array<SubMenuItem>();
        this.items.forEach(item => returnValue.push(item.buildSubMenuItem()));
        return returnValue;
    }

}

export class SubMenuItemBuilder {
    private target: SubMenuItem;
    private parent: SubMenuBuilder;


    constructor(label: string, parent: SubMenuBuilder) {
        this.target = new SubMenuItem(label);
        this.parent = parent;
        this.parent.items.push(this);
    }

    public item(label: string): SubMenuItemBuilder {
        return new SubMenuItemBuilder(label, this.parent);
    }

    public selected(): SubMenuItemBuilder {
        this.target.selected = true;
        return this;
    }

    public search(): SubMenuItemBuilder {
        this.target.hasSearchField = true;
        return this;
    }

    public buildSubMenuItem(): SubMenuItem {
        return this.target;
    }

    public buildSubMenu(): Array<SubMenuItem> {
        return this.parent.build();
    }
}
