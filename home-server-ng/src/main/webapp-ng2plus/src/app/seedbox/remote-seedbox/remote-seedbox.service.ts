import { ApiConstants } from './../../shared/api-constants.modele';
import { JsonTools } from './../../shared/json-tools.service';
import { RunningTorrent } from './models/running-torrent.modele';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';


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

}
