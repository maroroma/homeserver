import { TrackRenamer } from './../models/track-renamer.modele';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { TrackDescriptor } from './../models/track-descriptor.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { MusicService } from './../music.service';
import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
    selector: 'homeserver-track-list',
    templateUrl: 'track-list.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss', 'track-list.component.scss']
})

export class TrackListComponent implements OnInit {

    public albumDescriptor = new AlbumDescriptor();

    public trackList: VisualItemDataSource<TrackDescriptor>;


    /**
       * Construction de la datagrid
       * @memberOf PluginListComponent
       */
    public headers = SimpleDatagridHeaderBuilder
        .forField('item.trackNumber').display('track number').textBox().sortable()
        .forField('item.newFileName').display('file name').textBox().sortable()
        .forField('item.trackName').display('track name').textBox().sortable()
        .buildAll();

    @Output()
    public goToNextStep = new EventEmitter<any>();


    constructor(private musicService: MusicService, private notifyer: NotifyerService) {
        this.trackList = new VisualItemDataSource<TrackDescriptor>(visualItem => {
            visualItem.changeResolver = ((p1, p2) => {
                return p1.item.trackName !== p2.item.trackName
                    || p1.item.file.name !== p2.item.file.name
                    || p1.item.trackNumber !== p2.item.trackNumber;
            });
        });
    }

    ngOnInit() {
        this.albumDescriptor = this.musicService.currentAlbumDescriptor;
        this.musicService.getTracks().subscribe(res => {
            this.trackList.updateSourceList(res);
        });
    }

    public updateList(): void {
        this.musicService.getTracks().subscribe(res => {
            this.trackList.updateSourceList(res);
            this.notifyer.showSuccess('Liste rechargées');
        });
    }

    public autoTrackNumber(): void {

        this.trackList.updateSourceList(
            this.trackList.displayList.map((visualItem, index) => {
                const rawItem = visualItem.item;
                rawItem.trackNumber = index + 1;
                return rawItem;
            })
        );
    }

    public autoRenameFilenameFromTags(): void {
        this.trackList.updateSourceList(
            this.trackList.displayList.map(visualItem => {
                return TrackRenamer.renameFileFromTag(visualItem.item);
            })
        );
    }

    public updateAllTracks(): void {
        this.notifyer.waitingInfo('mise à jour des fichiers en cours');
        this.musicService.updateTracks(this.trackList.getRawItemsFromSource()).subscribe(res => {
            console.log('mise à jour terminée', res);
            this.goToNextStep.emit();
        });
    }
}
