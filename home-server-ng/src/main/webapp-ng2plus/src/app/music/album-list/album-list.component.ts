import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { MusicService } from '../music.service';
import { VisualItemDataSource } from '../../shared/visual-item-datasource.modele';
import { AlbumDescriptor } from '../models/album-descriptor.modele';

@Component({
    selector: 'homeserver-album-list',
    templateUrl: 'album-list.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss']
})
export class AlbumListComponent implements OnInit {

    @Output()
    public goToNextStep = new EventEmitter<any>();

    public dataSource = new VisualItemDataSource<AlbumDescriptor>();

    constructor(private musicService: MusicService) {

    }

    public addNewAlbum(): void {
        this.goToNextStep.emit();
    }

    ngOnInit() {
        this.musicService.getCompletedAlbums().subscribe(res => {
            this.dataSource.updateSourceList(res);
        });
    }
}