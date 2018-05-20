import { Subscription } from 'rxjs/Rx';
import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { ModuleStatus } from './module-status.modele';
import { ModuleHandler } from './module-handler.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { VisualItem } from './../../shared/visual-item.modele';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { AdministrationService } from './../administration.service';
import { Component, OnInit, Input, OnDestroy } from '@angular/core';

/**
 * Composant pour l'affichage des plugins.
 * @export
 * @class PluginListComponent
 * @implements {OnInit}
 */
@Component({
    selector: 'homeserver-plugin-list',
    templateUrl: 'plugin-list.component.html',
    styleUrls: ['plugin-list.component.scss']
})
export class PluginListComponent implements OnInit, OnDestroy {

    /**
     * Les des plugins à gérer.
     * @type {Array<VisualItem<ModuleHandler>>}
     * @memberOf PluginListComponent
     */
    public plugins: VisualItemDataSource<ModuleHandler>;

    public searchSubscription: Subscription;

    /**
     * Construction de la datagrid
     * @memberOf PluginListComponent
     */
    public headers = SimpleDatagridHeaderBuilder
        .forField('item.cssMenu').glyphicon()
        .forField('item.displayName').display('nom').sortable()
        .forField('item.moduleDescription').display('description').hideForSD().sortable()
        .forField('enabled').display('activé').onOff().sortable()
        .forAction().saveButton()
        .buildAll();

    /**
     * Creates an instance of PluginListComponent.
     * @param {AdministrationService} adminService
     * @memberOf PluginListComponent
     */
    constructor(private adminService: AdministrationService, private searchService: PageHeaderSearchService) {
        this.plugins = new VisualItemDataSource<ModuleHandler>(visualItem => {
            visualItem.enabled = visualItem.item.enabled;
            visualItem.readonly = visualItem.item.readOnly;
            visualItem.changeResolver = ((module1, module2) => {
                return module1.enabled !== module2.enabled;
            });
        }).sortByField('item.displayName');
    }

    /**
     * @memberOf PluginListComponent
     */
    public changeEventHandler() {
        console.log('changed');
    }

    /**
     * Sauvegarde d'un plugin
     * @param {VisualItem<ModuleHandler>} item
     * @memberOf PluginListComponent
     */
    public savePlugin(item: VisualItem<ModuleHandler>): void {
        item.item.enabled = item.enabled;
        this.adminService.updateModuleStatus(item.item, item.enabled)
            .subscribe(this.plugins.updateSourceList.bind(this.plugins));
    }

    /**
     * Sauvegarde d'un ensemble de plugins
     * @param {Array<VisualItem<ModuleHandler>>} items
     * @memberOf PluginListComponent
     */
    public savePlugins(items: Array<VisualItem<ModuleHandler>>): void {
        const input = new Array<ModuleStatus>();
        items.forEach(item => {
            input.push(new ModuleStatus(item.item.moduleId, item.enabled));
        });

        this.adminService.updateModulesStatus(input).subscribe(this.plugins.updateSourceList.bind(this.plugins));
    }

    /**
     * Init de la page.
     * @memberOf PluginListComponent
     */
    ngOnInit() {
        this.adminService.getAllModules().subscribe(this.plugins.updateSourceList.bind(this.plugins));
        this.searchSubscription = this.searchService.searchChanged.subscribe(res => this.plugins.filterByStringField(res, 'displayName'));
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }
}
