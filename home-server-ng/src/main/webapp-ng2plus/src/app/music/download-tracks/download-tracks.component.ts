import { ApiConstants } from './../../shared/api-constants.modele';
import { SimpleDatagridHeaderBuilder } from './../../common-gui/simple-datagrid/simple-datagrid-header-builder.modele';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { TrackDescriptor } from './../models/track-descriptor.modele';
import { MusicService } from './../music.service';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { AlbumDescriptor } from './../models/album-descriptor.modele';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'homeserver-download-tracks',
    templateUrl: 'download-tracks.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss']
})

export class DownloadTracksComponent implements OnInit {

    public albumDescriptor: AlbumDescriptor;

    public trackList: VisualItemDataSource<TrackDescriptor>;

    /**
       * Construction de la datagrid
       * @memberOf PluginListComponent
       */
    public headers = SimpleDatagridHeaderBuilder
        .forField('item.trackNumber').display('track number').sortable()
        .forField('item.file.name').display('file name').hideForSD().sortable()
        .forField('item.trackName').display('track name').sortable()
        .forAction()
        .exportButton()
        .buildAll();


    constructor(private musicService: MusicService, private notifyer: NotifyerService) {
        this.trackList = new VisualItemDataSource<TrackDescriptor>(visualItem => {
            visualItem.exportable = true;
            visualItem.exportUrl = ApiConstants.MUSIC_WORKING_DIR_API
                + '/' + this.musicService.currentAlbumDescriptor.directoryDescriptor.id
                + '/tracks/' + visualItem.item.file.id;
            visualItem.exportFileName = visualItem.item.file.name;
        });
    }

    ngOnInit() {
        this.albumDescriptor = this.musicService.currentAlbumDescriptor;
        this.musicService.getTracks().subscribe(res => {
            this.trackList.updateSourceList(res);
        });
    }
}
