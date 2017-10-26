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
        const rawList = new Array<UploadTrackCandidate>();
        for (let index = 0; index < files.files.length; index++) {
            rawList.push(new UploadTrackCandidate(files.files.item(index)));
        }

        this.filesToUpload.updateSourceList(rawList);

    }

    public deleteSelectedFiles(): void {
        this.filesToUpload.updateSourceList(this.filesToUpload.getRawUnselectedItems());
    }

    public uploadTracks(): void {
        this.notifyer.waitingInfo('upload en cours');
        this.musicService.sendTracks(this.filesToUpload.getRawItemsFromSource().map(candidate => candidate.basicFile))
            .subscribe(res => {
                console.log('upload terminé', res);
                this.goToNextStep.emit();
            });
    }
}