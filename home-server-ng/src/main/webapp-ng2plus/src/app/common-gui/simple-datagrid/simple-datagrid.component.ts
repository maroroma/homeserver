import { ImportedFiles } from './../import-file-button/imported-files.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { VisualItem } from './../../shared/visual-item.modele';
import { SimpleDatagridFieldDescriptor } from './simple-datagrid-field-descriptor.modele';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'homeserver-simple-datagrid',
    templateUrl: 'simple-datagrid.component.html',
    styleUrls: ['simple-datagrid.component.scss']
})
export class SimpleDatagridComponent implements OnInit {

    /**
     * Liste des données à afficher.
     * @type {Array<VisualItem<any>>}
     * @memberOf SimpleDatagridComponent
     */
    @Input()
    public dataSource: VisualItemDataSource<any>;

    /**
     * Configuration des champs à afficher.
     * @type {Array<SimpleDatagridFieldDescriptor>}
     * @memberOf SimpleDatagridComponent
     */
    @Input()
    public fieldConfigs: Array<SimpleDatagridFieldDescriptor>;

    @Output()
    public savedItem = new EventEmitter<VisualItem<any>>();

    @Output()
    public savedItems = new EventEmitter<Array<VisualItem<any>>>();

    @Output()
    public deletedItem = new EventEmitter<VisualItem<any>>();

    @Output()
    public zoomOnItem = new EventEmitter<VisualItem<any>>();

    @Output()
    public importFiles = new EventEmitter<ImportedFiles>();

    constructor() { }

    ngOnInit() { }


    /**
     * Appelé lorsqu'une modification est effectuée par un sous composant sur les données.
     * Cela permet de mettre en avant une modification sur la ligne.
     * @param {VisualItem<any>} item
     * 
     * @memberOf SimpleDatagridComponent
     */
    public changeEventHandler(item: VisualItem<any>): void {
        this.dataSource.checkForChange(item);
    }

    /**
     * Levée de l'event de sauvegarde d'un item.
     * @param {VisualItem<any>} item
     * @memberOf SimpleDatagridComponent
     */
    public innerSaveItem(item: VisualItem<any>): void {
        this.savedItem.emit(item);
    }

    /**
     * Levée de l'event de sauvegarde de plusieurs items.
     * @memberOf SimpleDatagridComponent
     */
    public innerSaveItems(): void {
        this.savedItems.emit(this.dataSource.getModifiedItems());
    }


    /**
     * Levée de l'event de suppression d'un item
     * @param {VisualItem<any>} item
     * @memberOf SimpleDatagridComponent
     */
    public innerDeleteItem(item: VisualItem<any>): void {
        this.deletedItem.emit(item);
    }

    /**
     * Levée de l'event pour l'import de fichiers.
     * @param {ImportedFiles} imported
     * @memberOf SimpleDatagridComponent
     */
    public innerImportFile(imported: ImportedFiles): void {
        this.importFiles.emit(imported);
    }
    /**
     * Déclenchement du tri
     * @param {SimpleDatagridFieldDescriptor} header
     * @memberOf SimpleDatagridComponent
     */
    public sortBy(header: SimpleDatagridFieldDescriptor): void {
        if (header.isSortable) {
            this.dataSource.sortByField(header.fieldName);
        }
    }

    /**
     * Déclenchement de la demande d'édition externe
     * @param {SimpleDatagridFieldDescriptor} header
     * @param {VisualItem<any>} data
     * 
     * @memberOf SimpleDatagridComponent
     */
    public zoomIn(header: SimpleDatagridFieldDescriptor, data: VisualItem<any>): void {
        if (header.zoomable) {
            this.zoomOnItem.emit(data);
        }
    }

}
