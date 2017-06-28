import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { Subscription } from 'rxjs/Rx';
import { VisualItem } from './../../shared/visual-item.modele';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { Cache } from './cache.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { AdministrationService } from './../administration.service';
import { Component, OnInit, Input, OnChanges, OnDestroy } from '@angular/core';

/**
 * Gestion des caches
 * @export
 * @class CacheListComponent
 * @implements {OnInit}
 * @implements {OnChanges}
 */
@Component({
    moduleId: module.id,
    selector: 'homeserver-cache-list',
    templateUrl: 'cache-list.component.html'
})
export class CacheListComponent implements OnInit, OnDestroy {

    public caches: VisualItemDataSource<Cache>;

    /**
     * PErmet de databindindé le champ de recherche
     * @memberOf PluginListComponent
     */
    public searchSubscription: Subscription;

    public headers = SimpleDatagridHeaderBuilder
        .forField('item.name').display('cache').sortable()
        .forField('item.nbElements').display('taille').sortable()
        .forAction().deleteButton()
        .buildAll();

    constructor(private adminService: AdministrationService, private searchService: PageHeaderSearchService) {
        this.caches = new VisualItemDataSource<Cache>();
    }

    ngOnInit() {
        this.adminService.loadAllCaches().subscribe(res => {
            this.caches.updateSourceList(res);
        });
        this.searchSubscription = this.searchService.searchChanged.subscribe(res => this.caches.filterByStringField(res, 'name'));
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }

    public deleteCache(item: VisualItem<Cache>): void {
        this.adminService.deleteCache(item.item).subscribe(res => {
            this.caches.updateSourceList(res);
        });
    }
}
