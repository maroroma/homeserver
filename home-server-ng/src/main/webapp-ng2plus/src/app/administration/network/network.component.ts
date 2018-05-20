import { UrlDescriptor } from './url-descriptor.modele';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { Subscription } from 'rxjs/Rx';
import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { PopupComponent } from './../../common-gui/popup/popup.component';
import { VisualItem } from './../../shared/visual-item.modele';
import { ServerDescriptor } from './server-descriptor.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { NetworkService } from './network.service';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';

@Component({
    selector: 'homeserver-network',
    templateUrl: 'network.component.html',
    styleUrls: ['network.component.scss', '../../common-gui/styles/edit-zone.scss']
})
export class NetworkComponent implements OnInit, OnDestroy {

    public serverDescriptors = new VisualItemDataSource<ServerDescriptor>();
    public urlDescriptors = new VisualItemDataSource<UrlDescriptor>();

    @ViewChild('popupAddServer')
    popupAddServer: PopupComponent;
    @ViewChild('popupDeleteServerConfirmation')
    popupDeleteServerConfirmation: PopupComponent;

    /**
       * Construction de la datagrid
       * @memberOf PluginListComponent
       */
    public headers = SimpleDatagridHeaderBuilder
        .forField('item.name').display('nom').sortable().hideForSD()
        .forField('item.url').display('url').sortable().link().hideForSD()
        .forField('item.url').display('url').sortable().link('item.name').hideForHD()
        .buildAll();

    /**
     * PErmet de databindindé le champ de recherche
     * @memberOf PluginListComponent
     */
    public searchSubscription: Subscription;

    public serverToDelete: VisualItem<ServerDescriptor>;

    constructor(private networkService: NetworkService, private searchService: PageHeaderSearchService) { }

    ngOnInit() {

        this.urlDescriptors.sortByField('item.name');
        this.serverDescriptors.sortByField('item.nom');
        
        this.networkService.loadServers().subscribe(res => this.regenerateAllLists(res));
        this.searchSubscription = this.searchService.searchChanged.subscribe(res => this.serverDescriptors.filterByStringField(res, 'nom'));
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }

    public displayCreationPanel(): void {
        this.popupAddServer.display();
    }

    public saveItem(item: VisualItem<ServerDescriptor>): void {
        this.networkService.saveServer(item.item).subscribe(res =>
            this.regenerateAllLists(res)
        );
    }

    public deleteItem(item: VisualItem<ServerDescriptor>): void {
        this.serverToDelete = item;
        this.popupDeleteServerConfirmation.display();
        // this.networkService.deleteServer(item.item).subscribe(res => this.regenerateAllLists(res));
    }

    public confirmDelete(): void {
        this.networkService.deleteServer(this.serverToDelete.item).subscribe(res => this.regenerateAllLists(res));
    }

    public innerCreate(server: ServerDescriptor): void {
        this.networkService.createServer(server).subscribe(res => this.regenerateAllLists(res));
    }

    private regenerateAllLists(rawDescriptors: Array<ServerDescriptor>): void {
        this.serverDescriptors.updateSourceList(rawDescriptors);
        this.regenerateUrlList();
    }

    private regenerateUrlList(): void {
        const finalList = new Array<UrlDescriptor>();
        this.serverDescriptors.sourceList
            // récup d'une liste de liste d'url
            .map(visualItem => visualItem.item.urls)
            // pour chacune des url, rajout unitaire dans la liste finale
            .forEach(urlList => urlList.forEach(oneUrl => finalList.push(oneUrl)));

        this.urlDescriptors.updateSourceList(finalList);
    }
}

