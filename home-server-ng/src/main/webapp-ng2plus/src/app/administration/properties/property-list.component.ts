import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { Subscription } from 'rxjs/Rx';
import { FilterTools } from './../../shared/filter-tools.service';
import { PopupComponent } from './../../common-gui/popup/popup.component';
import { VisualItem } from './../../shared/visual-item.modele';
import { Property } from './property.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { AdministrationService } from './../administration.service';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';

@Component({
    selector: 'homeserver-property-list',
    templateUrl: 'property-list.component.html'
})
export class PropertyListComponent implements OnInit, OnDestroy {

    /**
     * Liste des propriétés manipulées.
     * @type {VisualItemDataSource<Property>}
     * @memberOf PropertyListComponent
     */
    properties: VisualItemDataSource<Property>;

    @ViewChild('popup')
    popup: PopupComponent;

    propertyToEdit: VisualItem<Property>;

    /**
       * Construction de la datagrid
       * @memberOf PluginListComponent
       */
    public headers = SimpleDatagridHeaderBuilder
        .forField('item.displayName').display('nom').hideForHD().sortable().zoomable()
        .forField('item.id').display('nom').hideForSD().sortable().zoomable()
        .forField('item.value').display('valeur').textBox().hideForSD()
        .forAction().saveButton().hideForSD()
        .buildAll();

    /**
     * PErmet de databindindé le champ de recherche
     * @memberOf PluginListComponent
     */
    public searchSubscription: Subscription;

    constructor(private adminService: AdministrationService, private searchService: PageHeaderSearchService) {
        this.properties = new VisualItemDataSource<Property>(visualItem => {
            visualItem.readonly = visualItem.item.readOnly;
            visualItem.changeResolver = ((p1, p2) => p1.item.value !== p2.item.value);
        });
    }

    /**
     * Sauvegarde d'une propriété.
     * @param {VisualItem<Property>} item
     * @memberOf PropertyListComponent
     */
    public saveProperty(item: VisualItem<Property>): void {
        this.adminService.saveProperty(item.item).subscribe(res => {
            this.properties.updateSourceList(res);
        });
    }

    public editProperty(item: VisualItem<Property>): void {
        this.propertyToEdit = item;
        this.popup.display();
    }

    public saveEditedProperty(): void {
        this.saveProperty(this.propertyToEdit);
    }

    public cancelEdit(): void {
        this.propertyToEdit.restaure((initialItem, actualItem) => {
            actualItem.item.value = initialItem.item.value;
        });
    }

    ngOnInit() {
        this.adminService.loadAllProperties().subscribe(res => {
            this.properties.updateSourceList(res);
        });
        this.searchSubscription = this.searchService.searchChanged.subscribe(res => this.properties.filterByStringField(res, 'id'));
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }
}

