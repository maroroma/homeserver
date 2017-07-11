import { TimerObservable } from 'rxjs/observable/TimerObservable';
import { Observable } from 'rxjs/Observable';
import { VisualItem } from './../../shared/visual-item.modele';
import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { Subscription } from 'rxjs/Rx';
import { RunningTorrent } from './models/running-torrent.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { RemoteSeedBoxService } from './remote-seedbox.service';
import { Component, OnInit, OnDestroy, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-remote-seedbox',
    templateUrl: 'remote-seedbox.component.html',
    styleUrls: ['remote-seedbox.component.scss']
})

export class RemoteSeedBoxComponent implements OnInit, OnDestroy {

    public runningTorrents = new VisualItemDataSource<RunningTorrent>();

    searchSubscription: Subscription;
    timerSubscription: Subscription;

    @Output()
    public gotoCompletedFiles = new EventEmitter<any>();

    constructor(private remoteService: RemoteSeedBoxService, private searchService: PageHeaderSearchService) { }

    ngOnInit() {

        // les appels se font toutes les secondes, ça évite de se coltiner l'interface sse pour le TS.
        this.timerSubscription = TimerObservable.create(0, 1000).subscribe(tres => {
            this.remoteService.getRunningTorrents().subscribe(res => this.runningTorrents.updateSourceList(res));
        });


        this.searchSubscription = this.searchService.searchChanged
            .subscribe(search => this.runningTorrents.filterByStringField(search, 'name'));
    }

    ngOnDestroy() {
        this.searchSubscription.unsubscribe();
        this.timerSubscription.unsubscribe();
    }

    public toggleSelection(item: VisualItem<RunningTorrent>): void {
        // désactiver pour le moment, sinon la sélection saute sur le rechargement des données.
        // this.runningTorrents.toggleItemSelection(item);
    }
}
