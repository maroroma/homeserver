import { Component, OnInit, OnDestroy } from "@angular/core";
import { IotService } from "../iot.service";
import { AbstractIotComponent } from "../models/abstract-iot-component.modele";
import { VisualItemDataSource } from "app/shared/visual-item-datasource.modele";
import { NotifyerService } from "app/common-gui/notifyer/notifyer.service";
import { PageHeaderSearchService } from "app/common-gui/page-header/page-header-search.service";
import { Subscription } from "rxjs";
import { FilterTools } from "app/shared/filter-tools.service";
import { SimpleDatagridHeaderBuilder } from "app/common-gui/simple-datagrid/simple-datagrid-header-builder.modele";
import { VisualItem } from "app/shared/visual-item.modele";

@Component({
    selector: 'homeserver-iot-management',
    templateUrl: 'iot-management.component.html',
    styleUrls: ['../../common-gui/styles/shared.scss', '../../common-gui/styles/edit-zone.scss']
})
export class IotManagementComponent implements OnInit, OnDestroy {


    iotComponents:VisualItemDataSource<AbstractIotComponent>;

    /**
       * Construction de la datagrid
       * @memberOf IotManagementComponent
       */
      public headers = SimpleDatagridHeaderBuilder
      .forField('item.glyphicon').glyphicon()
      .forField('item.componentDescriptor.id').display('id').hideForSD()
      .forField('item.componentDescriptor.name').display('nom').sortable().textBox()
      .forField('item.componentDescriptor.componentType').display('type').hideForSD()
      .forField('item.componentDescriptor.ipAddress').display('ip')
      .forField('item.available').display('disponible').hideForSD()
      .forAction().saveButton().deleteButton()
      .buildAll();

    public searchSubscription: Subscription;

    constructor(private iotService:IotService, private notifyer:NotifyerService, private searchService: PageHeaderSearchService) {
        this.iotComponents = new VisualItemDataSource<AbstractIotComponent>(visualItem => {
            visualItem.changeResolver = (item1, item2) => 
            item1.item.componentDescriptor.name !== item2.item.componentDescriptor.name
        });

    }

    ngOnInit() {
        this.loadComponentList();
        this.searchSubscription = this.searchService.searchChanged
        .subscribe(res => this.iotComponents.filterBy(FilterTools.simpleFilter(res, 'componentDescriptor.name')));
    }

    loadComponentList() {
        this.iotService.getAllIotComponents()
        .subscribe(res => {
            this.iotComponents.updateSourceList(res);
            console.log(this.iotComponents.displayList);
            this.notifyer.showSuccess('liste de composants chargée');
        });
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }

    updateList():void {
        this.loadComponentList();
    }

    deleteComponent(componentToDelete:VisualItem<AbstractIotComponent>) {
        this.iotService.deleteComponent(componentToDelete.item).subscribe(res => {
            this.notifyer.showSuccess('composant supprimé');
            this.iotComponents.updateSourceList(res);
        });
    }

    updateComponent(componentToUpdate:VisualItem<AbstractIotComponent>) {
        this.iotService.updateComponent(componentToUpdate.item).subscribe(res => {
            this.notifyer.showSuccess('composant mis à jour');
            this.iotComponents.updateSourceList(res);
        })
    }


}