import { TimerObservable } from 'rxjs/observable/TimerObservable';
import { Observable } from 'rxjs/Observable';
import { VisualItem } from './../../shared/visual-item.modele';
import { PageHeaderSearchService } from './../../common-gui/page-header/page-header-search.service';
import { Subscription } from 'rxjs/Rx';
import { RunningTorrent } from './models/running-torrent.modele';
import { VisualItemDataSource } from './../../shared/visual-item-datasource.modele';
import { RemoteSeedBoxService } from './remote-seedbox.service';
import { Component, OnInit, OnDestroy, EventEmitter, Output, ViewChild } from '@angular/core';
import { AdministrationService } from '../../administration/administration.service';
import { PopupComponent } from '../../common-gui/popup/popup.component';
import { NewTorrent } from './models/new-torrent.modele';
import { NotifyerService } from '../../common-gui/notifyer/notifyer.service';

@Component({
    selector: 'homeserver-remote-seedbox',
    templateUrl: 'remote-seedbox.component.html',
    styleUrls: ['remote-seedbox.component.scss', '../../common-gui/styles/edit-zone.scss']
})

export class RemoteSeedBoxComponent implements OnInit, OnDestroy {

    public runningTorrents = new VisualItemDataSource<RunningTorrent>(visualItem => {
        visualItem.id = visualItem.item.id;
    });

    searchSubscription: Subscription;
    timerSubscription: Subscription;

    @ViewChild('popupAddTorrent')
    popupAddTorrent: PopupComponent;

    @Output()
    public gotoCompletedFiles = new EventEmitter<any>();

    public addTorrentRequest = new NewTorrent();
    public currentMagLink = "";

    constructor(private remoteService: RemoteSeedBoxService, private searchService: PageHeaderSearchService,
        private administrationService: AdministrationService,
        private notifyer: NotifyerService) { }

    ngOnInit() {

        // les appels se font sur la base de la fréquence paramétrée sur le serveur
        this.administrationService.getProperty('homeserver.seedbox.client.stream.fixedDelay')
            .subscribe(property => {
                this.timerSubscription = TimerObservable.create(0, Number(property.value)).subscribe(tres => {
                    this.remoteService.getRunningTorrents()
                        .subscribe(res => {
                            this.runningTorrents.updateSourceList(res, true);
                        });
                });
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
        this.runningTorrents.toggleItemSelection(item);
    }

    public displayAddTorrent(): void {
        this.addTorrentRequest.clear();
        this.popupAddTorrent.display();
    }

    public addMagnetLink(): void {
        this.addTorrentRequest.magnetLinks.push(this.currentMagLink);
        this.currentMagLink = "";
    }

    public confirmAdd(): void {
        this.remoteService.addNewTorrents(this.addTorrentRequest).subscribe();
    }

    public removeSelectedTorrents(): void {
        this.remoteService
            .removeTorrents(this.runningTorrents.getRawSelectedItems())
            .subscribe(() => this.notifyer.showInfo('Demande de suppression des torrents émise'));
    }
}
