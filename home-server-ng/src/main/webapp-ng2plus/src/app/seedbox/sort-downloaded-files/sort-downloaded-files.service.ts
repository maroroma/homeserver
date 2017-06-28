import { MovedFile } from './models/moved-file.modele';
import { TargetDirectory } from './models/target-directory.modele';
import { CompletedFile } from './models/completed-file.modele';
import { MoveRequestBuilder } from './models/move-request-builder.modele';
import { ApiConstants } from './../../shared/api-constants.modele';
import { Subject } from 'rxjs/Subject';
import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { Observable } from 'rxjs/Rx';
import { JsonTools } from './../../shared/json-tools.service';
import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';

/**
 * Service de gestion des fichies à trier.
 * @export
 * @class SortDownloadedFilesService
 */
@Injectable()
export class SortDownloadedFilesService {

    public moveRequestBuilder: MoveRequestBuilder;

    constructor(private http: Http, private notifyer: NotifyerService) {
        this.moveRequestBuilder = new MoveRequestBuilder();
    }

    /**
     * Chargement des fichiers à trier.
     * @returns {Observable<MoveRequestBuilder>}
     * @memberOf SortDownloadedFilesService
     */
    public loadCompletedFiles(): Observable<MoveRequestBuilder> {
        return this.http.get(ApiConstants.SEEDBOX_TODO_COMPLETED_FILES_API)
            .map(res => {
                return JsonTools.map(res.json(), CompletedFile.dfFromRaw);
            }).map(res => {
                this.moveRequestBuilder.completedFiles.updateSourceList(res);
                return this.moveRequestBuilder;
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des fichiers complétés');
                return Observable.of(this.moveRequestBuilder);
            });
    }

    /**
     * PErmet de charger les différentes cibles pour le déplacement des fichiers.
     * @returns {Observable<Array<TargetDirectory>>}
     * @memberOf SortDownloadedFilesService
     */
    public loadTargets(): Observable<Array<TargetDirectory>> {
        return this.http.get(ApiConstants.SEEDBOX_TARGETS_API)
            .map(res => {
                return JsonTools.map(res.json(), TargetDirectory.dfFromRaw);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des cibles');
                return Observable.of(new Array<TargetDirectory>());
            });
    }

    public deleteCompletedFiles(filesToDelete: Array<CompletedFile>): Observable<MoveRequestBuilder> {
        const responses = new Array<Observable<Response>>();

        // préparation des x appels de suppression
        filesToDelete.forEach(fileToDelete => {
            responses.push(this.http.delete(ApiConstants.SEEDBOX_TODO_COMPLETED_FILES_API + '/' + fileToDelete.id));
        });


        // lancement des x appels en paralleles, avec attente de synchro pour la récupération finale des résultats.
        return Observable.forkJoin(responses)
            .map(res => {
                this.notifyer.showSuccess('Les fichiers ont bien été supprimés');
                return JsonTools.map(res[res.length - 1].json(), CompletedFile.dfFromRaw);
            })
            .map(res => {
                this.moveRequestBuilder.completedFiles.updateSourceList(res);
                return this.moveRequestBuilder;
            });

    }

    public commitMove(): Observable<Array<MovedFile>> {

        return this.http.post(ApiConstants.SEEDBOX_SORT_API, this.moveRequestBuilder.buildRequest())
            .map(res => {
                return JsonTools.map(res.json(), MovedFile.fromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors du déplacement des fichiers');
                return Observable.of(new Array<MovedFile>());
            });

    }


}
