import { Component, ViewChild, OnInit, OnDestroy } from '@angular/core';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { FileBrowserDownloadComponent } from '../../common-gui/file-browser/file-browser-download/file-browser-download.component';
import { ActivatedRoute, Params } from '@angular/router';
import { Subscription } from 'rxjs';
import { FileManagerService } from '../filemanager.service';
import { FileManagerResolver } from '../filemanager-resolver.modele';
import { FileDescriptor } from '../../shared/file-descriptor.modele';

@Component({
    moduleId: module.id,
    selector: 'homeserver-direct-download',
    templateUrl: 'direct-download.component.html',
})
export class DirectDownloadComponent implements OnInit, OnDestroy {

    /**
     * Affichage des fichiers à télécharger.
     */
    @ViewChild('popupDownloadFiles')
    popupDownloadFiles: FileBrowserDownloadComponent;

    paramsSubscription: Subscription;

    fileResolver = new FileManagerResolver();

    fileList = new Array<FileDescriptor>();

    constructor(private activatedRoute: ActivatedRoute, private fileManagerService: FileManagerService) {

    }

    ngOnInit() {

        this.paramsSubscription = this.activatedRoute
            // récupération des paramètres
            .queryParams
            .flatMap((params: Params) => {
                // récupération du filedescriptor correspondant au fichir demandé
                return this.fileManagerService.getFileDescriptor(params['id']);
            })
            .subscribe(fileDescriptor => {
                // alimentation de la liste pour le composant graphique
                this.fileList.length = 0;
                this.fileList.push(fileDescriptor);
                console.debug(this.fileList);
            });

    }

    ngOnDestroy() {
        this.paramsSubscription.unsubscribe();
    }
}