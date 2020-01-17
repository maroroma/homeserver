import { Component, OnInit, OnDestroy } from "@angular/core";
import { IotService } from "../iot.service";
import { AbstractIotComponent } from "../models/abstract-iot-component.modele";
import { VisualItemDataSource } from "app/shared/visual-item-datasource.modele";
import { NotifyerService } from "app/common-gui/notifyer/notifyer.service";
import { PageHeaderSearchService } from "app/common-gui/page-header/page-header-search.service";
import { Subscription } from "rxjs";
import { FilterTools } from "app/shared/filter-tools.service";

@Component({
    selector: 'homeserver-iot-board',
    templateUrl: 'iot-board.component.html',
    styleUrls: ['iot-board.component.scss','../../common-gui/styles/shared.scss', '../../common-gui/styles/edit-zone.scss']
})
export class IotBoardComponent implements OnInit, OnDestroy {


    iotComponents = new VisualItemDataSource<AbstractIotComponent>();

    public searchSubscription: Subscription;

    constructor(private iotService:IotService, private notifyer:NotifyerService, private searchService: PageHeaderSearchService) { }

    ngOnInit() {
        this.loadComponentList();
        this.searchSubscription = this.searchService.searchChanged
        .subscribe(res => this.iotComponents.filterBy(FilterTools.simpleFilter(res, 'componentDescriptor.name')));
    }

    loadComponentList() {
        this.iotService.getAllIotComponents()
        .subscribe(res => {
            this.iotComponents.updateSourceList(res);
            this.notifyer.showSuccess('liste de composants chargée');
        });
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }

    updateList():void {
        this.loadComponentList();
    }

    sendBuzz(iotComponent:AbstractIotComponent) {
        this.iotService.sendBuzz(iotComponent.componentDescriptor.id)
        .subscribe(res => console.log(res));

    }
}