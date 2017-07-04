import { FilterTools } from './filter-tools.service';
import { SortTools } from './sort-tools.service';
import { ReflectionTools } from './reflection-tools.service';
import { VisualItem } from './visual-item.modele';
export class VisualItemDataSource<T> {

    /**
     * Liste de base.
     * @type {Array<VisualItem<T>>}
     * @memberOf VisualItemDataSource
     */
    public sourceList: Array<VisualItem<T>>;

    /**
     * Liste triée, filtrée qui est utilisée pour l'affichage.
     * @type {Array<VisualItem<T>>}
     * @memberOf VisualItemDataSource
     */
    public displayList: Array<VisualItem<T>>;

    private filterPredicate: (item: T) => boolean;


    public changeOccured = false;
    public nbItemChanged = 0;
    public nbItemSelected = 0;
    public hasItemSelected = false;

    public isReverseSort: boolean;
    public sortField: string;

    private propertiesMapper: (visualItem: VisualItem<T>) => void;

    // private enabledResolver: (item: T) => boolean;
    // private readOnlyResolver: (item: T) => boolean;
    // public changeResolver: (item1: VisualItem<T>, item2: VisualItem<T>) => boolean;

    constructor(propertiesMapper?: (visualItem: VisualItem<T>) => void) {
        if (propertiesMapper) {
            this.propertiesMapper = propertiesMapper;
        }
    }

    public getModifiedItems(): Array<VisualItem<T>> {
        return this.sourceList.filter(item => item.changeOccured);
    }

    public checkForChange(item: VisualItem<T>): void {
        item.checkForChange();
        this.nbItemChanged = this.getModifiedItems().length;
        this.changeOccured = this.nbItemChanged > 0;
    }

    /**
     * Change le status de sélection d'un item.
     * @param {VisualItem<T>} item
     * @memberOf VisualItemDataSource
     */
    public toggleItemSelection(item: VisualItem<T>): void {
        item.toggleSelection();
        this.updateSelectionStatus();
    }

    public selectOneItem(item: VisualItem<T>): void {
        this.unselectAll();
        item.toggleSelection();
        this.updateSelectionStatus();
    }

    /**
     * Retourne la liste d'item sélectionné.
     * @returns {Array<VisualItem<T>>}
     * @memberOf VisualItemDataSource
     */
    public getSelectedItem(): Array<VisualItem<T>> {
        return this.sourceList.filter(item => item.selected);
    }

    /**
     * Retourne la liste d'item sélectionné en tant que nouvelle datasource.
     */
    public getSelectedItemsAsVisualDataSource(): VisualItemDataSource<T> {
        const returnValue = new VisualItemDataSource<T>();
        returnValue.updateSourceList(this.getSelectedItem().map(item => item.item));
        return returnValue;
    }

    /**
     * Concatene cette datasource avec la datasource en entrée.
     * @param {VisualItemDataSource<T>} otherDataSource
     * @returns {VisualItemDataSource<T>}
     * @memberof VisualItemDataSource
     */
    public concat(otherDataSource: VisualItemDataSource<T>): VisualItemDataSource<T> {
        const returnValue = new VisualItemDataSource<T>();

        returnValue.updateSourceList(this.getRawItemsFromSource().concat(otherDataSource.getRawItemsFromSource()));

        return returnValue;
    }

    /**
     * Retourne la liste des items bruts de la liste.
     * @returns {Array<T>} 
     * @memberof VisualItemDataSource
     */
    public getRawItemsFromSource(): Array<T> {
        return this.sourceList.map(vi => vi.item);
    }

    /**
     * Retourne la liste des items sélectionnés en tant que liste simple.
     * @returns {Array<T>}  -
     * @memberof VisualItemDataSource
     */
    public getRawSelectedItems(): Array<T> {
        return this.sourceList.filter(vi => vi.selected).map(vi => vi.item);
    }

    /**
     * Déselectionne tous les items.
     * @memberOf VisualItemDataSource
     */
    public unselectAll(): void {
        this.sourceList.forEach(item => item.selected = false);
        this.updateSelectionStatus();
    }

    /**
     * Sélectionne l'ensemble des items affichés.
     */
    public selectAllDisplayedItems() {
        this.displayList.forEach(item => item.selected = true);
        this.updateSelectionStatus();
    }

    /**
     * Permet de mettre à jour les indicateurs liées à la sélection.
     * @private
     * @memberOf VisualItemDataSource
     */
    public updateSelectionStatus() {
        const selectedItems = this.getSelectedItem();
        this.nbItemSelected = selectedItems.length;
        this.hasItemSelected = this.nbItemSelected > 0;
    }

    public updateSourceList(rawItems: Array<T>): void {
        this.sourceList = VisualItem.of(rawItems, this.propertiesMapper);
        this.updateDisplayList();
        this.updateSelectionStatus();
    }

    public filterBy(predicate: (item: T) => boolean): void {
        this.filterPredicate = predicate;
        this.updateDisplayList();
    }

    public clearFilter(): void {
        this.filterPredicate = undefined;
        this.updateDisplayList();
    }

    /**
     * Permet de filtrer sur champ de type string, pour un champ donné et une valeur de filtrage.
     * @param {string} searchField
     * @param {string} fieldName
     * @memberOf VisualItemDataSource
     */
    public filterByStringField(searchField: string, fieldName: string): void {
        this.filterBy(FilterTools.simpleFilter(searchField, fieldName));
    }


    /**
     * Permet de trier pour un champ donné.
     * @param {string} fieldName
     * @memberOf VisualItemDataSource
     */
    public sortByField(fieldName: string): void {
        if (this.sortField === fieldName) {
            this.isReverseSort = !this.isReverseSort;
        } else {
            this.isReverseSort = false;
        }
        this.sortField = fieldName;
        this.updateDisplayList();
    }


    private updateDisplayList() {
        if (this.sourceList) {
            if (this.filterPredicate !== undefined) {
                this.displayList = this.sourceList
                    .filter(this.filter.bind(this))
                    .sort(SortTools.sorter(this.sortField, this.isReverseSort));
            } else {
                // this.displayList = this.sourceList.sort(this.sort.bind(this));
                this.displayList = this.sourceList.sort(SortTools.sorter(this.sortField, this.isReverseSort));
            }


        }
    }




    /**
     * Predicate pour le filtrage.
     * Permet de résoudre automatiquement l'item du VisualItem pour faciliter la déclaration
     * de la méthode de filtrage.
     * @private
     * @param {VisualItem<T>} item
     * @returns {boolean}
     * @memberOf VisualItemDataSource
     */
    private filter(item: VisualItem<T>): boolean {
        return this.filterPredicate(item.item);
    }


}
