import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { ImportedFiles } from './../../common-gui/import-file-button/imported-files.modele';
import { MusicService } from './../music.service';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-add-albumart',
    templateUrl: 'add-albumart.component.html',
    styleUrls: ['add-albumart.component.scss', '../../common-gui/styles/edit-zone.scss']
})

export class AddAlbumArtComponent implements OnInit {

    public albumDescriptor: AlbumDescriptor;

    @Output()
    public goToNextStep = new EventEmitter<any>();

    constructor(private musicService: MusicService) { }

    ngOnInit() {
        this.albumDescriptor = this.musicService.currentAlbumDescriptor;
    }

    public startUploadFile(file: ImportedFiles): void {
        this.musicService.addAlbumArt(file).subscribe(res => {
            this.albumDescriptor = this.musicService.currentAlbumDescriptor;
        });
    }

    public gotoUploadTracks(): void {
        this.goToNextStep.emit();
    }
}
