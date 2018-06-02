import { JsonTools } from './json-tools.service';
export class VisualItem<T> {
    public selected = false;
    public enabled = false;
    public readonly = false;
    public exportable = false;
    public exportUrl: string;
    public exportFileName: string;
    public item: T;
    public changeOccured = false;
    public id: any;


    /**
     * Fonction appelée pour déterminer si un changement au eu lieu sur l'item
     * @memberOf VisualItem
     */
    public changeResolver: (item1: VisualItem<T>, item2: VisualItem<T>) => boolean;

    private initialState: string;


    public static of<U>(rawList: Array<U>,
        propertiesMapper?: (visualItem: VisualItem<U>) => void
    ): Array<VisualItem<U>> {
        const returnValue = new Array<VisualItem<U>>();

        if (rawList !== undefined) {
            rawList.forEach(item => returnValue.push(new VisualItem(item, propertiesMapper)));
        }
        return returnValue;
    }

    constructor(item?: T,
        propertiesMapper?: (visualItem: VisualItem<T>) => void
    ) {
        this.item = item;
        // this.initialState = JSON.stringify(this);

        if (propertiesMapper) {
            propertiesMapper(this);
        }


        this.initialState = JSON.stringify(this);
    }

    public checkForChange(): void {
        this.changeOccured = this.changeResolver(JSON.parse(this.initialState), this);
    }

    public restaure(restaureFunc: (initialItem: VisualItem<T>, actualItem: VisualItem<T>) => void) {
        restaureFunc(JSON.parse(this.initialState), this);
    }

    public toggleSelection(): void {
        this.selected = !this.selected;
    }

    public autoRestaure() {
        const backup = JSON.parse(this.initialState);
        this.changeOccured = false;
        this.enabled = backup.enabled;
        this.readonly = backup.readonly;
        this.item = backup.item;
        this.selected = backup.selected;
        this.exportable = backup.exportable;
        this.exportUrl = backup.exportUrl;
        this.exportFileName = backup.exportFileName;
        this.id = backup.id;
    }

}
