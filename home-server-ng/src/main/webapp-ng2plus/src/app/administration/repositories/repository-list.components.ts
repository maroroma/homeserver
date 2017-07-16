import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { Subscription } from 'rxjs/Rx';
import { VisualItem } from './../../shared/visual-item.modele';
import { ImportedFiles } from './../../common-gui/import-file-button/imported-files.modele';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { AdministrationService } from './../administration.service';
import { Repository } from './repository.modele';
import { Component, OnInit, Input, OnChanges, OnDestroy } from '@angular/core';

@Component({
    selector: 'homeserver-repository-list',
    templateUrl: 'repository-list.component.html'
})
export class RepositoryListComponent implements OnInit, OnDestroy {

    repositories: VisualItemDataSource<Repository>;

    /**
       * Construction de la datagrid
       * @memberOf PluginListComponent
       */
    public headers = SimpleDatagridHeaderBuilder
        .forField('item.id').display('id').hideForSD().sortable()
        .forField('item.shortId').display('id').hideForHD().sortable()
        .forField('item.filename').display('fichier').hideForSD().sortable()
        .forField('item.nbItems').display('nombre d\'éléments').hideForSD().sortable()
        .forAction().exportButton().importButton()
        .buildAll();

    /**
     * PErmet de databindindé le champ de recherche
     * @memberOf PluginListComponent
     */
    public searchSubscription: Subscription;

    constructor(private adminService: AdministrationService, private searchService: PageHeaderSearchService) {
        this.repositories = new VisualItemDataSource<Repository>(visualItem => {
            visualItem.exportable = visualItem.item.exists;
            visualItem.exportUrl = visualItem.item.downloadUrl;
            visualItem.exportFileName = visualItem.item.filename;
        });
    }

    ngOnInit() {
        this.adminService.loadAllRepositories().subscribe(res => {
            this.repositories.updateSourceList(res);
        });

        this.searchSubscription = this.searchService.searchChanged.subscribe(res => this.repositories.filterByStringField(res, 'id'));
    }

    public importRepository(imported: ImportedFiles): void {
        console.log('import !', imported);
        this.adminService.uploadRepository((imported.associatedItem as VisualItem<Repository>).item, imported.files[0])
            .subscribe(res => { this.repositories.updateSourceList(res); });
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }
}
