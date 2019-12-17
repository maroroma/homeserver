import { UploadTrackCandidate } from './../models/upload-track-candidate.modele';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { FileBrowserComponent } from './../../common-gui/file-browser/file-browser.component';
import { FileManagerComponent } from './../../filemanager/filemanager.component';
import { FileManagerResolver } from './../../filemanager/filemanager-resolver.modele';
import { FileBrowserOptionsBuilder } from './../../common-gui/file-browser/file-browser-options-builder.modele';
import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';
import { ImportedFiles } from './../../common-gui/import-file-button/imported-files.modele';
import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { MusicService } from './../music.service';
import { Component, OnInit, ViewChild, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'homeserver-upload-tracks',
    templateUrl: 'upload-tracks.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss', 'upload-tracks.component.scss']
})

export class UploadTracksComponent implements OnInit {

    public albumDescriptor = new AlbumDescriptor();


    public filesToUpload = new VisualItemDataSource<UploadTrackCandidate>();

    @Output()
    public goToNextStep = new EventEmitter<any>();

    constructor(private musicService: MusicService, private notifyer: NotifyerService) {
    }

    ngOnInit() {
        this.albumDescriptor = this.musicService.currentAlbumDescriptor;
    }

    public prepareUploadFile(files: ImportedFiles): void {
        this.filesToUpload.updateSourceList(files.files.map(oneFile => new UploadTrackCandidate(oneFile)));
    }

    public deleteSelectedFiles(): void {
        this.filesToUpload.updateSourceList(this.filesToUpload.getRawUnselectedItems());
    }

    public uploadTracks(): void {

        const nbFileToUpload = this.filesToUpload.getRawItemsFromSource().length;

        this.notifyer.waitingInfo('upload du fichier 1/' + nbFileToUpload);
        const chainCall =
            this.musicService.sendTracks(this.filesToUpload.getRawItemsFromSource().map(candidate => candidate.basicFile));

        chainCall.allItemCompleted.subscribe(res => {
            this.notifyer.showSuccess('Tous les fichiers ont bien été uploadés');
            this.goToNextStep.emit();
        });

        chainCall.oneItemCompleted.subscribe(res => {
            this.notifyer.waitingInfo('upload du fichier ' + res.item2 + '/' + nbFileToUpload);
        });
    }
}
