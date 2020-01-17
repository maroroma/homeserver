import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { NotifyerService } from "app/common-gui/notifyer/notifyer.service";
import { IotComponentFactory } from "./iot-component-factory.service";
import { Observable } from "rxjs";
import { AbstractIotComponent } from "./models/abstract-iot-component.modele";
import { ApiConstants } from "app/shared/api-constants.modele";
import { JsonTools } from "app/shared/json-tools.service";
import { BuzzRequest } from "./models/buzz-request.modele";

@Injectable()
export class IotService {
    constructor(private http: Http, private notifyer: NotifyerService, private iotFactory:IotComponentFactory) { }


    getAllIotComponents():Observable<Array<AbstractIotComponent>> {
        return this.http.get(ApiConstants.IOT_ALL_COMPONENTS_API)
            .map(res => {
                return JsonTools.map(res.json(), this.iotFactory.deserializeIotComponent.bind(this.iotFactory));
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des iotComponents');
                return Observable.of(null);
            });
    }

    sendBuzz(buzzerId:string) {
        const request = new BuzzRequest();
        request.id = buzzerId;
        return this.http.post(ApiConstants.IOT_BUZZ_REQUEST, request)
            .map(response => {
                this.notifyer.showSuccess('demande de buzz émise');
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la création d\'un serveur');
                return Observable.of(false);
            });
    }
    

}