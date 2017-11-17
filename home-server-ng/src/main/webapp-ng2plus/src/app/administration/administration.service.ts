import { HomeServerStatus } from './power/homeserver-status.modele';
import { Observable } from 'rxjs/Observable';
import { Cache } from './caches/cache.modele';
import { ImportedFiles } from './../common-gui/import-file-button/imported-files.modele';
import { Repository } from './repositories/repository.modele';
import { NotifyerService } from './../common-gui/notifyer/notifyer.service';
import { JsonTools } from './../shared/json-tools.service';
import { Property } from './properties/property.modele';
import { SortTools } from './../shared/sort-tools.service';
import { ModuleStatus } from './plugins/module-status.modele';
import { ModuleHandler } from './plugins/module-handler.modele';
import { ApiConstants } from './../shared/api-constants.modele';
import { Http } from '@angular/http';
import { Injectable } from '@angular/core';

@Injectable()
export class AdministrationService {

    /**
     * Liste de tous les modules
     * @memberOf AdministrationService
     */
    public allModules = new Array<ModuleHandler>();
    /**
     * Liste de tous les modules actifs.
     * @memberOf AdministrationService
     */
    public enabledModules = new Array<ModuleHandler>();
    /**
     * Liste de tous les modules affichés dans le menu.
     * @memberOf AdministrationService
     */
    public displayedModules = new Array<ModuleHandler>();


    constructor(private http: Http, private notifyer: NotifyerService) { }

    /**
     * Récupération simple de la liste de modules.
     * @returns {Observable<Array<ModuleHandler>>}
     * @memberOf AdministrationService
     */
    public getAllModules(): Observable<Array<ModuleHandler>> {
        return this.http.get(ApiConstants.ADMIN_MODULES_API)
            .map(response => {
                const rawResponse = response.json();
                const returnValue = new Array<ModuleHandler>();
                for (const rawModule of rawResponse) {
                    returnValue.push(ModuleHandler.fromRaw(rawModule));
                }
                return returnValue;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des modules');
                return Observable.of(new Array<ModuleHandler>());
            });
    }

    /**
     * Mise à jour du status d'un module.
     * @param {ModuleHandler} module
     * @param {boolean} newStatus
     * @returns {Observable<Array<ModuleHandler>>}
     * @memberOf AdministrationService
     */
    public updateModuleStatus(module: ModuleHandler, newStatus: boolean): Observable<Array<ModuleHandler>> {
        return this.http.patch(ApiConstants.ADMIN_MODULE_API + '/' + module.moduleId,
            module)
            .flatMap(response => {
                return this.loadAllModules();
            })
            .map(response => {
                this.notifyer.successfullSave();
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la mise à jour d\'un module');
                return Observable.of(new Array<ModuleHandler>());
            });
    }

    /**
     * Mise à jour d'un ensemble de status de modules.
     * @param {Array<ModuleStatus>} modules
     * @returns {Observable<Array<ModuleHandler>>}
     * @memberOf AdministrationService
     */
    public updateModulesStatus(modules: Array<ModuleStatus>): Observable<Array<ModuleHandler>> {

        return this.http.patch(ApiConstants.ADMIN_MODULES_API, modules)
            .flatMap(response => {
                return this.loadAllModules();
            })
            .map(response => {
                this.notifyer.successfullSave();
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la mise à jour de plusieurs modules');
                return Observable.of(new Array<ModuleHandler>());
            });

    }

    /**
     * Chargement des modules, actifs ou non.
     * @returns {Observable<Array<ModuleHandler>>}
     * @memberOf AdministrationService
     */
    public loadAllModules(): Observable<Array<ModuleHandler>> {
        return this.http.get(ApiConstants.ADMIN_MODULES_API)
            .map(response => {
                this.allModules.length = 0;
                this.enabledModules.length = 0;
                this.displayedModules.length = 0;
                const rawResponse = response.json();
                for (const rawModule of rawResponse) {


                    const moduleHandler = ModuleHandler.fromRaw(rawModule);

                    this.allModules.push(moduleHandler);

                    if (moduleHandler.enabled) {
                        this.enabledModules.push(moduleHandler);

                        if (moduleHandler.hasClientSide) {
                            this.displayedModules.push(moduleHandler);
                        }
                    }
                }

                this.sortDisplayableModules();

                return this.allModules;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des modules');
                return Observable.of(new Array<ModuleHandler>());
            });
    }

    /**
     * Chargement de l'ensemble des properties
     * @returns {Observable<Array<Property>>}
     * @memberOf AdministrationService
     */
    public loadAllProperties(): Observable<Array<Property>> {
        return this.http.get(ApiConstants.ADMIN_CONFIGS_API)
            .map(response => {
                return JsonTools.map(response.json(), Property.fromRaw);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des propriétés');
                return Observable.of(new Array<Property>());
            });
    }

    /**
     * Sauvegarde d'une propriété
     * @param {Property} propertyToSave
     * @returns {Observable<Array<Property>>}
     * @memberOf AdministrationService
     */
    public saveProperty(propertyToSave: Property): Observable<Array<Property>> {
        return this.http.patch(ApiConstants.ADMIN_CONFIG_API + '/' + propertyToSave.id, propertyToSave)
            .flatMap(response => {
                return this.loadAllProperties();
            })
            .map(response => {
                this.notifyer.successfullSave();
                return response;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la sauvegarde d\'une propriété');
                return Observable.of(new Array<Property>());
            });
    }

    /**
     * Récupération des repositories
     * @returns {Observable<Array<Repository>>}
     * @memberOf AdministrationService
     */
    public loadAllRepositories(): Observable<Array<Repository>> {
        return this.http.get(ApiConstants.ADMIN_REPOS_API)
            .map(response => {
                return JsonTools.map(response.json(), Repository.fromRaw);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des repositories');
                return Observable.of(new Array<Repository>());
            });
    }

    /**
     * Upload d'un repo
     * @param {Repository} repo
     * @param {File} file
     * @returns {Observable<Array<Repository>>}
     * @memberOf AdministrationService
     */
    public uploadRepository(repo: Repository, file: File): Observable<Array<Repository>> {
        const fd = new FormData();
        fd.append('file', file);
        return this.http.post(ApiConstants.ADMIN_REPO_API + '/' + repo.id + '/import', fd)
            .flatMap(reponse => {
                return this.loadAllRepositories();
            })
            .map(res => {
                this.notifyer.successfullUpload(file);
                return res;
            });
    }

    /**
     * Permet de lister l'ensemble des caches.
     * @returns {Observable<Array<Cache>>}
     * @memberOf AdministrationService
     */
    public loadAllCaches(): Observable<Array<Cache>> {
        return this.http.get(ApiConstants.ADMIN_CACHES_API)
            .map(response => {
                return JsonTools.map(response.json(), Cache.fromRaw);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des caches');
                return Observable.of(new Array<Cache>());
            });
    }

    /**
     * Permet de vider un cache.
     * @param {Cache} item
     * @returns {Observable<Array<Cache>>}
     * @memberOf AdministrationService
     */
    public deleteCache(item: Cache): Observable<Array<Cache>> {
        return this.http.delete(ApiConstants.ADMIN_CACHES_API + '/' + item.name + '/keys')
            .flatMap(response => this.loadAllCaches())
            .map(res => {
                this.notifyer.showSuccess('cache ' + item.name + ' vidé avec succes');
                return res;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors du vidage d\'un caches');
                return Observable.of(new Array<Cache>());
            });
    }

    public shutdown(): Observable<any> {
        return this.http.patch(ApiConstants.ADMIN_SHUT_API, null).map(res => true)
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de l\'arrêt du serveur');
                return Observable.of(new Array<Cache>());
            });
    }

    /**
     * Méthode de tri par défaut.
     * @private
     * @memberOf AdministrationService
     */
    private sortDisplayableModules() {
        this.displayedModules = this.displayedModules.sort(SortTools.asc('displayName'));
    }

    /**
     * Trouve un module par son identifiant.
     * @param {string} moduleId
     * @returns {ModuleHandler}
     * @memberOf AdministrationService
     */
    public findModuleById(moduleId: string): ModuleHandler {
        return this.allModules.find(handler => handler.moduleId === moduleId);
    }

    /**
     * Retourne le status du server.
     */
    public getServerStatus(): Observable<HomeServerStatus> {

        return this.http.get(ApiConstants.ADMIN_STATUS_API).map(res => {
            return HomeServerStatus.mapFromJson(res.json());
        }).catch((err, data) => {
            this.notifyer.showError('Une erreur est survenue lors de la récupération du status du server');
            return Observable.of(new HomeServerStatus());
        });
    }
}
