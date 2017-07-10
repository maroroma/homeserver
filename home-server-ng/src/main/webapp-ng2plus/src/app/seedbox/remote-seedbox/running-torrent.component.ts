import { VisualItem } from './../../shared/visual-item.modele';
import { RunningTorrent } from './models/running-torrent.modele';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-running-torrent',
    templateUrl: 'running-torrent.component.html',
    styleUrls: ['running-torrent.component.scss']
})
export class RunningTorrentComponent implements OnInit {

    @Input()
    public torrent: VisualItem<RunningTorrent>;

    constructor() { }

    ngOnInit() { }

}
