import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { NotifyerService } from "app/common-gui/notifyer/notifyer.service";
import { IotComponentFactory } from "./iot-component-factory.service";
import { Observable } from "rxjs";
import { AbstractIotComponent } from "./models/abstract-iot-component.modele";
import { ApiConstants } from "app/shared/api-constants.modele";
import { JsonTools } from "app/shared/json-tools.service";
import { BuzzRequest } from "./models/buzz-request.modele";
import { MiniSprite } from "./models/mini-sprite.modele";

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

    getAllMiniSprites():Observable<Array<MiniSprite>> {
        return this.http.get(ApiConstants.IOT_ALL_SPRITES_API)
            .map(res => {
                return JsonTools.map(res.json(), MiniSprite.fromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des minisprites');
                return Observable.of(null);
            });
    }

    deleteMiniSprite(spriteToDelete:MiniSprite):Observable<Array<MiniSprite>> {
        return this.http.delete(ApiConstants.IOT_ALL_SPRITES_API + '/' + spriteToDelete.name)
            .map(res => {
                return JsonTools.map(res.json(), MiniSprite.fromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la suppression du minisprite');
                return Observable.of(null);
            });
    }

    saveNewMiniSprite(newSprite:MiniSprite):Observable<Array<MiniSprite>> {
        return this.http.post(ApiConstants.IOT_ALL_SPRITES_API, newSprite)
            .map(res => {
                return JsonTools.map(res.json(), MiniSprite.fromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la création du nouveau sprite');
                return Observable.of(null);
            });
    }

    updateMiniSprite(newSprite:MiniSprite):Observable<Array<MiniSprite>> {
        return this.http.put(ApiConstants.IOT_ALL_SPRITES_API, newSprite)
            .map(res => {
                return JsonTools.map(res.json(), MiniSprite.fromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la mise à jour du sprite');
                return Observable.of(null);
            });
    }

    sendBuzz(request:BuzzRequest) {
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