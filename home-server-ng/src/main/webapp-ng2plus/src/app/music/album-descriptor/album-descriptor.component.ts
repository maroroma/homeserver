import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { MusicService } from './../music.service';
import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'homeserver-album-descriptor',
    templateUrl: 'album-descriptor.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss']
})

export class AlbumDescriptorComponent implements OnInit {

    public albumDescriptor = new AlbumDescriptor();

    @Output()
    public goToNextStep = new EventEmitter<any>();

    constructor(private musicService: MusicService, private notifyer: NotifyerService) { }

    public gotoAlbumArtSelection() {
        this.musicService.prepareWorkingDirectory(this.albumDescriptor).subscribe(res => {
            if (res !== null) {
                this.goToNextStep.emit();
            } else {
                this.notifyer.showError('Réessayer avec un autre nom...');
            }
        });
    }

    ngOnInit() { }
}