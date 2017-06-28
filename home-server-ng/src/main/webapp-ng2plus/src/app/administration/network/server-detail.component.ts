import { FilterTools } from './../../shared/filter-tools.service';
import { UrlDescriptor } from './url-descriptor.modele';
import { ServerDescriptor } from './server-descriptor.modele';
import { VisualItem } from './../../shared/visual-item.modele';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-server-detail',
    templateUrl: 'server-detail.component.html',
    styleUrls: ['server-detail.component.scss']
})
export class ServerDetailComponent implements OnInit {

    @Input()
    serverItem: VisualItem<ServerDescriptor>;

    @Output()
    save = new EventEmitter<VisualItem<any>>();
    @Output()
    delete = new EventEmitter<VisualItem<any>>();

    newService = new UrlDescriptor();

    constructor() { }

    ngOnInit() { }

    public innerSave(item: VisualItem<ServerDescriptor>): void {
        this.save.emit(item);
    }
    public innerDelete(item: VisualItem<ServerDescriptor>): void {
        this.delete.emit(item);
    }

    public addService(): void {
        this.serverItem.item.urls.push(this.newService);
        this.newService = new UrlDescriptor();
    }

    public removeService(item: UrlDescriptor): void {
        this.serverItem.item.urls = FilterTools.removeAny(this.serverItem.item.urls, oneItem => item.url === oneItem.url);
    }

}
