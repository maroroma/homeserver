import { FileBrowserOptions } from './file-browser-options.modele';
import { NotifyerService } from './../notifyer/notifyer.service';
import { Subject } from 'rxjs/Subject';
import { VisualItem } from './../../shared/visual-item.modele';
import { DirectoryCreationRequest } from './directory-creation-request.modele';
import { PopupComponent } from './../popup/popup.component';
import { Subscription, Observable } from 'rxjs/Rx';
import { PageHeaderSearchService } from './../page-header/page-header-search.service';
import { FilterTools } from './../../shared/filter-tools.service';
import { FileBrowserResolver } from './file-browser-resolver.modele';
import { FileBrowserService } from './file-browser.service';
import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';
import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { Component, OnInit, Input, OnChanges, SimpleChanges, OnDestroy, EventEmitter, Output, ViewChild } from '@angular/core';

@Component({
    selector: 'homeserver-file-browser',
    templateUrl: 'file-browser.component.html',
    styleUrls: ['file-browser.component.scss']
})
export class FileBrowserComponent implements OnInit, OnChanges, OnDestroy {


    /**
     * Liste des fichiers affichés dans la stack supérieure.
     * @memberOf FileBrowserComponent
     */
    pathStack = new Array<DirectoryDescriptor>();


    /**
     * Utilisé pour éviter les effets de clignotement des données lors de la navigation
     * @memberOf FileBrowserComponent
     */
    tempPathStack = new Array<DirectoryDescriptor>();

    /**
     * Liste des répertoires du répertoires en cul de stack
     * @memberOf FileBrowserComponent
     */
    directoryList = new VisualItemDataSource<FileDescriptor>();

    /**
     * Liste des fichiers du répertoire courant.
     * @memberOf FileBrowserComponent
     */
    fileList = new VisualItemDataSource<FileDescriptor>();

    /**
     * Détermine si un répertoire ou un fichier est sélectionné.
     * @memberOf FileBrowserComponent
     */
    someFileSelected = false;

    /**
     * Nombre de fichiers sélectionnés.
     */
    nbFilesSelected = 0;

    /**
     * Event levé lorsque la sélection d'un répertoire change
     * @memberOf FileBrowserComponent
     */
    @Output()
    public selectedDirectoryChange = new EventEmitter<DirectoryDescriptor>();

    @Input()
    public options: FileBrowserOptions;

    public dirCreationRequest = new DirectoryCreationRequest();

    /**
     * Répertoire de démarrage.
     * @type {DirectoryDescriptor}
     * @memberOf FileBrowserComponent
     */
    @Input()
    public starterDirectory: DirectoryDescriptor;

    // @Input()
    // gestion de la recherche à travers des events.
    searchSubscription: Subscription;
    clearSearchSubscription: Subscription;

    /**
     * Popup pour la création d'un répertoire
     * @type {PopupComponent}
     * @memberOf FileBrowserComponent
     */
    @ViewChild('popupCreateDirectory')
    popupCreateDirectory: PopupComponent;

    /**
     * Popup pour la confirmation de suppression d'un fichier.
     * @type {PopupComponent}
     * @memberOf FileBrowserComponent
     */
    @ViewChild('popupDeleteFile')
    popupDeleteFile: PopupComponent;

    constructor(private browserService: FileBrowserService, private searchService: PageHeaderSearchService,
        private notifyer: NotifyerService) { }

    ngOnInit() {
        // mise en place de la recherche active à partir du service dédié
        this.searchSubscription = this.searchService.searchChanged.subscribe(res => {
            this.directoryList.filterByStringField(res, 'name');
            this.fileList.filterByStringField(res, 'name');
        });
        this.clearSearchSubscription = this.searchService.searchCleared.subscribe(res => {
            this.directoryList.clearFilter();
            this.fileList.clearFilter();
        });
    }

    ngOnChanges(changes?: SimpleChanges) {

        if (changes) {
            if (changes.starterDirectory && this.starterDirectory) {
                this.reloadFromStart();
            }
        } else {
            this.reloadFromStart();
        }
    }

    ngOnDestroy(): void {
        this.searchSubscription.unsubscribe();
        this.clearSearchSubscription.unsubscribe();
    }

    /**
     * REtourne à la racine de démarrage de l'explorateur de fichier.
     */
    private reloadFromStart(): void {
        this.tempPathStack.length = 0;
        this.tempPathStack.push(this.starterDirectory);
        this.regeneratePathStack();
    }

    /**
     * Sélection d'un sous répertoire.
     * @param {DirectoryDescriptor} subDirectory
     * @memberOf FileBrowserComponent
     */
    public selectSubDirectory(subDirectory: DirectoryDescriptor): Observable<DirectoryDescriptor> {
        const returnValue = new Subject<DirectoryDescriptor>();
        this.reinitSelection();
        if (subDirectory.id === this.starterDirectory.id) {
            this.ngOnChanges();
        } else {
            this.browserService.getDirectoryDetail(subDirectory, this.options.resolver).subscribe(res => {
                this.tempPathStack.push(res);
                this.regeneratePathStack();
                returnValue.next(res);
            });
        }

        return returnValue;
    }

    public selectSubDirectoryFromList(subDirectory: DirectoryDescriptor): void {
        this.regenerateTmpStack();
        this.selectSubDirectory(subDirectory);
    }

    /**
     * Sélection d'un sous répertoire depuis la stack.
     * @param {DirectoryDescriptor} oneDirectory
     * @memberOf FileBrowserComponent
     */
    public selectSubDirectoryFromStack(oneDirectory: DirectoryDescriptor): void {
        const newStack = new Array<DirectoryDescriptor>();
        const indexSelected = FilterTools.indexOf(this.pathStack, item => oneDirectory.id === item.id);
        this.tempPathStack = this.pathStack.filter((item, index) => index < indexSelected);
        this.selectSubDirectory(oneDirectory);
    }

    /**
     * Sélection du répertoire parent.
     * @memberOf FileBrowserComponent
     */
    public selectParentDirectory(): void {
        this.regenerateTmpStack();
        this.tempPathStack.pop();
        const parentDirectory = this.tempPathStack.pop();
        this.selectSubDirectory(parentDirectory);
    }

    /**
     * REtourne le dernier élément de la stack.
     * @returns {DirectoryDescriptor}
     * @memberOf FileBrowserComponent
     */
    public getLastPathStack(): DirectoryDescriptor {
        return FilterTools.last(this.pathStack);
    }

    /**
     * MIse à jour du dernier element de la stack.
     * @param {DirectoryDescriptor} updated
     * 
     * @memberOf FileBrowserComponent
     */
    public updateLastPathStack(updated: DirectoryDescriptor): void {
        this.tempPathStack[this.tempPathStack.length] = updated;
    }

    /**
     * Génération de la stack temporaire
     * @memberOf FileBrowserComponent
     */
    private regenerateTmpStack(): void {
        this.tempPathStack = this.pathStack.filter(item => true);
    }


    /**
     * Génération de la stack finale sur la base de la stack temporaire
     * @private
     * @memberOf FileBrowserComponent
     */
    private regeneratePathStack(): void {
        this.reinitSelection();
        this.pathStack = this.tempPathStack.filter(item => true);
        this.directoryList.updateSourceList(FilterTools.last(this.pathStack).directories);
        this.fileList.updateSourceList(FilterTools.last(this.pathStack).files);
        this.searchService.clearSearchField();
        this.selectedDirectoryChange.emit(this.getLastPathStack());
    }


    public refreshCurrentDirectory(): void {
        this.regenerateTmpStack();
        const toUpdate = this.tempPathStack.pop();
        this.selectSubDirectory(toUpdate).subscribe(res => this.notifyer.showInfo('liste mise à jour'));
    }

    public displayDirectoryCreation(): void {
        this.popupCreateDirectory.display();
    }

    public confirmCreation(): void {
        console.log('confirmation creation repertoire', this.dirCreationRequest);
        this.dirCreationRequest.parentDirectory = this.getLastPathStack();
        this.browserService.createDirectory(this.dirCreationRequest, this.options.resolver).subscribe(res => {

            this.dirCreationRequest.reinit();
            this.refreshCurrentDirectory();
        });
    }

    /**
     * Selection d'un item dans la liste donnée
     */
    public selectItem(event: Event, list: VisualItemDataSource<FileDescriptor>, fileD: VisualItem<FileDescriptor>) {
        // on stop l'event pour empecher l'affichage du contenu du répertoire.
        event.stopImmediatePropagation();
        list.toggleItemSelection(fileD);
        this.someFileSelected = this.directoryList.hasItemSelected || this.fileList.hasItemSelected;
        this.nbFilesSelected = this.directoryList.nbItemSelected + this.fileList.nbItemSelected;
    }

    public reinitSelection(): void {
        this.someFileSelected = false;
        this.nbFilesSelected = 0;
    }

    /**
     * Affichage de la popup de confirmation de suppression
     */
    public displayDirectoryDeleteConfirmation(): void {
        this.popupDeleteFile.display();
    }

    /**
     * Lancement de la suppression une fois confirméée
     * @memberOf FileBrowserComponent
     */
    public confirmDeletion(): void {

        // construction de la liste finale de fichiers à supprimer
        // par concaténation de la liste de répertoires sélectionné
        const finalList = this.directoryList.getSelectedItem().map(vi => vi.item)
            // et de la liste de fichiers sélectionnés.
            .concat(this.fileList.getSelectedItem().map(vi => vi.item));

        this.browserService.deleteFiles(this.getLastPathStack(),
            finalList,
            this.options.resolver
        ).subscribe(res => {
            this.refreshCurrentDirectory();
        });
    }
}
