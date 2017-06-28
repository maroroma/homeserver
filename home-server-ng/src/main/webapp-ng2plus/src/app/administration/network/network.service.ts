import { JsonTools } from './../../shared/json-tools.service';
import { ApiConstants } from './../../shared/api-constants.modele';
import { Http } from '@angular/http';
import { ServerDescriptor } from './server-descriptor.modele';
import { Observable } from 'rxjs/Rx';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { Injectable } from '@angular/core';


/**
 * 
 * Permet de gérer les descripteurs de serveurs.
 * @export
 * @class NetworkService
 */
@Injectable()
export class NetworkService {

    constructor(private notifyer: NotifyerService, private http: Http) { }


    /**
     * Chargement de l'ensemble des serveurs.
     * @returns {Observable<Array<ServerDescriptor>>}
     * @memberOf NetworkService
     */
    public loadServers(): Observable<Array<ServerDescriptor>> {
        return this.http.get(ApiConstants.NETWORK_SERVERS_API)
            .map(res => JsonTools.map(res.json(), ServerDescriptor.fromRaw))
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des serveurs');
                return Observable.of(new Array<ServerDescriptor>());
            });
    }

    /**
     * Permet de charger la liste des serveurs disponibles.
     * @returns {Observable<Array<ServerDescriptor>>}
     * @memberOf NetworkService
     */
    public loadAvailableServers(): Observable<Array<ServerDescriptor>> {
        return this.http.get(ApiConstants.NETWORK_AVAILABLE_SERVER_API)
            .map(res => JsonTools.map(res.json(), ServerDescriptor.fromRaw))
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des serveurs disponibles');
                return Observable.of(new Array<ServerDescriptor>());
            });
    }

    /**
     * Permet de sauvegarder les modifications d'un serveur.
     * @param {ServerDescriptor} serverToSave
     * @returns {Observable<Array<ServerDescriptor>>}
     * @memberOf NetworkService
     */
    public saveServer(serverToSave: ServerDescriptor): Observable<Array<ServerDescriptor>> {
        return this.http.patch(ApiConstants.NETWORK_SERVER_API + '/' + serverToSave.nom, serverToSave)
            .flatMap(response => {
                return this.loadServers();
            })
            .map(response => {
                this.notifyer.successfullSave();
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la sauvegarde d\'un serveur');
                return Observable.of(new Array<ServerDescriptor>());
            });
    }

    /**
     * Permet de créer un serveur.
     * @param {ServerDescriptor} serverToSave
     * @returns {Observable<Array<ServerDescriptor>>}
     * @memberOf NetworkService
     */
    public createServer(serverToSave: ServerDescriptor): Observable<Array<ServerDescriptor>> {
        return this.http.post(ApiConstants.NETWORK_SERVER_API, serverToSave)
            .flatMap(response => {
                return this.loadServers();
            })
            .map(response => {
                this.notifyer.successfullSave();
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la création d\'un serveur');
                return Observable.of(new Array<ServerDescriptor>());
            });
    }

    /**
     * Suppression d'un serveur.
     * @param {ServerDescriptor} serverToDelete
     * @returns {Observable<Array<ServerDescriptor>>}
     * @memberOf NetworkService
     */
    public deleteServer(serverToDelete: ServerDescriptor): Observable<Array<ServerDescriptor>> {
        return this.http.delete(ApiConstants.NETWORK_SERVER_API + '/' + serverToDelete.nom)
            .flatMap(response => {
                return this.loadServers();
            })
            .map(response => {
                this.notifyer.successfullDelete();
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la suppression d\'un serveur');
                return Observable.of(new Array<ServerDescriptor>());
            });
    }
}
