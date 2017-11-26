import { JsonTools } from './../shared/json-tools.service';
import { ApiConstants } from './../shared/api-constants.modele';
import { KioskDisplayOption } from './model/kiosk-display-options.modele';
import { Observable } from 'rxjs/Rx';
import { Http } from '@angular/http';
import { Injectable } from '@angular/core';
import { NotifyerService } from 'app/common-gui/notifyer/notifyer.service';

@Injectable()
export class KioskService {

    constructor(private http: Http, private notifyer: NotifyerService) { }

    public getDisplayOptions(): Observable<KioskDisplayOption> {
        return this.http.get(ApiConstants.KIOSK_DISPLAY_OPTIONS_API).map(res => {
            return KioskDisplayOption.fromRaw(res.json());
        })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des options du mode kiosk');
                return Observable.of(null);
            });
    }
}