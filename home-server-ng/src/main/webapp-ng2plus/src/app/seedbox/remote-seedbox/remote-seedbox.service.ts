import { ApiConstants } from './../../shared/api-constants.modele';
import { JsonTools } from './../../shared/json-tools.service';
import { RunningTorrent } from './models/running-torrent.modele';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { Injectable } from '@angular/core';
import { Http, Response, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { NewTorrent } from './models/new-torrent.modele';
import { TorrentsToDelete } from './models/torrents-to-delete.modele';


@Injectable()
export class RemoteSeedBoxService {
    constructor(private http: Http, private notifyer: NotifyerService) { }

    public getRunningTorrents(): Observable<Array<RunningTorrent>> {
        return this.http.get(ApiConstants.SEEDBOX_TORRENTS_API).map(res => {
            return JsonTools.map(res.json(), RunningTorrent.fromRaw);
        }).catch((err, data) => {
            this.notifyer.showError('Une erreur est survenue lors de la récupération des torrents en cours');
            return Observable.of(new Array<RunningTorrent>());
        });
    }

    public addNewTorrents(newTorrent: NewTorrent): Observable<boolean> {
        return this.http.post(ApiConstants.SEEDBOX_TORRENTS_API, newTorrent)
            .map(res => true)
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de l\'ajout de nouveau torrent');
                return Observable.of(false);
            });
    }

    public removeTorrents(torrentsToRemove: Array<RunningTorrent>): Observable<boolean> {
        return this.http.delete(ApiConstants.SEEDBOX_TORRENTS_API, new RequestOptions({
            body: new TorrentsToDelete(torrentsToRemove.map(oneTorrent => Number(oneTorrent.id)))
        }))
            .map(res => true)
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la suppression des torrents');
                return Observable.of(false);
            });
    }

}
