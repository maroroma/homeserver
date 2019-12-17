import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { MusicService } from '../music.service';
import { VisualItemDataSource } from '../../shared/visual-item-datasource.modele';
import { AlbumDescriptor } from '../models/album-descriptor.modele';
import { VisualItem } from 'app/shared/visual-item.modele';
import { NotifyerService } from 'app/common-gui/notifyer/notifyer.service';

@Component({
    selector: 'homeserver-album-list',
    templateUrl: 'album-list.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss', 'album-list.component.scss']
})
export class AlbumListComponent implements OnInit {

    @Output()
    public goToNextStep = new EventEmitter<any>();

    public dataSource = new VisualItemDataSource<AlbumDescriptor>();

    constructor(private musicService: MusicService, private notifyer: NotifyerService) {

    }

    public addNewAlbum(): void {
        this.goToNextStep.emit();
    }

    ngOnInit() {
        this.musicService.getCompletedAlbums().subscribe(res => {
            this.dataSource.updateSourceList(res);
        });
    }


    public selectOneAlbum(oneAlbum: VisualItem<AlbumDescriptor>): void {
        this.dataSource.selectOneItem(oneAlbum);
    }

    public archiveAlbum(albumDescriptor: AlbumDescriptor) {
        this.notifyer.waitingInfo('archivage des fichiers en cours');
        this.musicService.archiveAlbum(albumDescriptor).subscribe(res =>
            this.notifyer.showSuccess('album archivé')
        );

    }
}