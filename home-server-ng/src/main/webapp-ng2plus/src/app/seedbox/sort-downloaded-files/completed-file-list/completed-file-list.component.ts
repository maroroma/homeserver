import { Subscription } from 'rxjs/Rx';
import { PageHeaderSearchService } from './../../../common-gui/page-header/page-header-search.service';
import { FilterTools } from './../../../shared/filter-tools.service';
import { VisualItem } from './../../../shared/visual-item.modele';
import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { NotifyerService } from './../../../common-gui/notifyer/notifyer.service';
import { SortDownloadedFilesService } from './../sort-downloaded-files.service';
import { PopupComponent } from './../../../common-gui/popup/popup.component';
import { Tuple } from './../../../shared/tuple.modele';
import { CompletedFile } from './../models/completed-file.modele';
import { VisualItemDataSource } from './../../../shared/visual-item-datasource.modele';

import { Component, OnInit, Input, OnChanges, OnDestroy, ViewChild, EventEmitter, Output } from '@angular/core';

/**
 * Permet de sélectionner les fichiers à trier.
 * @export
 * @class CompletedFileListComponent
 * @implements {OnInit}
 * @implements {OnChanges}
 */
@Component({
    selector: 'homeserver-completed-file-list',
    templateUrl: 'completed-file-list.component.html',
    styleUrls: ['completed-file-list.component.scss', '../../../common-gui/styles/edit-zone.scss']
})
export class CompletedFileListComponent implements OnInit, OnChanges, OnDestroy {


    /**
     * Liste des fichiers complétés
     * @type {VisualItemDataSource<CompletedFile>}
     * @memberOf CompletedFileListComponent
     */
    completedFiles: VisualItemDataSource<CompletedFile>;


    /**
     * Liste des types de fichiers disponibles
     * @memberOf CompletedFileListComponent
     */
    availableFileTypes = new VisualItemDataSource<Tuple<string, (fd: FileDescriptor) => boolean>>();

    searchSubscription: Subscription;

    /**
     * Popup pour la gestion de la suppression
     * @type {PopupComponent}
     * @memberOf CompletedFileListComponent
     */
    @ViewChild('popupConfirmation')
    popupStopping: PopupComponent;


    @Output()
    public goToNextStep = new EventEmitter<any>();

    public nbFileToDelete = 0;

    constructor(private sortService: SortDownloadedFilesService, private notifyer: NotifyerService,
        private searchService: PageHeaderSearchService) { }

    ngOnInit() {
        this.sortService.loadCompletedFiles().subscribe(res => {
            this.completedFiles = res.completedFiles;
            this.updateAvailableFileTypes();
        });

        this.searchSubscription = this.searchService.searchChanged.subscribe(search => this.applyComplexFilter());
    }

    ngOnChanges() {
        if (this.completedFiles) {
            this.applyComplexFilter();
        }
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
    }

    updateList() {
        this.sortService.loadCompletedFiles().subscribe(res => {
            this.completedFiles = res.completedFiles;
            this.notifyer.showInfo('Liste mise à jour');
        });
    }

    deleteSelectedFiles(): void {
        this.nbFileToDelete = this.completedFiles.nbItemSelected;
        this.popupStopping.display();
    }

    confirmDeletion(): void {
        this.sortService.deleteCompletedFiles(this.completedFiles.getSelectedItem().map(visualItem => visualItem.item))
            .subscribe(res => this.completedFiles = res.completedFiles);
    }

    goToTargetSelection(): void {
        this.sortService.moveRequestBuilder.backupCompletedFiles();
        this.completedFiles.clearFilter();
        this.searchService.clearSearchField();
        this.goToNextStep.emit();
    }



    /**
     * Mise à jour des types de fichiers disponibles pour le filtrage avancé.
     * @memberOf CompletedFileListComponent
     */
    updateAvailableFileTypes(): void {
        const fileTypes = new Array<Tuple<string, (fd: FileDescriptor) => boolean>>();
        if (this.completedFiles.sourceList.filter(item => FileDescriptor.isMusicFile(item.item)).length > 0) {
            fileTypes.push(new Tuple('music', FileDescriptor.isMusicFile));
        }
        if (this.completedFiles.sourceList.filter(item => FileDescriptor.isVideoFile(item.item)).length > 0) {
            fileTypes.push(new Tuple('film', FileDescriptor.isVideoFile));
        }
        if (this.completedFiles.sourceList.filter(item => FileDescriptor.isCommonFile(item.item)).length > 0) {
            fileTypes.push(new Tuple('file', FileDescriptor.isCommonFile));
        }

        this.availableFileTypes.updateSourceList(fileTypes);
    }

    fileTypeFilterToggle(vi: VisualItem<string>): void {
        this.applyComplexFilter();
    }

    /**
     * Application de filtre complexe, à partir du type de fichier.
     * @private
     * @memberOf CompletedFileListComponent
     */
    private applyComplexFilter(): void {
        this.availableFileTypes.updateSelectionStatus();

        const fileNamePredicate = FilterTools.simpleFilter(this.searchService.searchData, 'name');

        const fileExtensionPredidate = (item: CompletedFile) => {
            if (this.availableFileTypes.hasItemSelected) {
                return FilterTools.or(this.availableFileTypes.getSelectedItem().map(selectedVi => selectedVi.item.item2))(item);
            } else {
                return true;
            }
        };

        this.completedFiles.filterBy(FilterTools.and([fileNamePredicate, fileExtensionPredidate]));
    }
}
