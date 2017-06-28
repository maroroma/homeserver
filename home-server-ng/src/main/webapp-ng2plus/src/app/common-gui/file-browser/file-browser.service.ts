import { ApiConstants } from './../../shared/api-constants.modele';
import { FileDeletionResult } from './file-deletion-result.modele';
import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { NotifyerService } from './../notifyer/notifyer.service';
import { DirectoryCreationRequest } from './directory-creation-request.modele';
import { JsonTools } from './../../shared/json-tools.service';
import { FileBrowserResolver } from './file-browser-resolver.modele';
import { Observable } from 'rxjs/Rx';
import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';
import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';

/**
 * Service utilisé par le file browser pour réaliser ces appels https de manipulation de fichiers.
 * @export
 * @class FileBrowserService
 */
@Injectable()
export class FileBrowserService {

    constructor(private http: Http, private notifyer: NotifyerService) { }

    /**
     * Récupération des détails d'un répertoire
     * @param {DirectoryDescriptor} directory
     * @param {FileBrowserResolver} resolver
     * @returns {Observable<DirectoryDescriptor>}
     * 
     * @memberOf FileBrowserService
     */
    public getDirectoryDetail(directory: DirectoryDescriptor, resolver: FileBrowserResolver): Observable<DirectoryDescriptor> {
        return this.http.get(resolver.directoryDetailsUri(directory)).map(res => {
            return DirectoryDescriptor.dfFromRaw(res.json());
        });
        // return Observable.of(directory);
    }

    public createDirectory(creationRequest: DirectoryCreationRequest, resolver: FileBrowserResolver): Observable<DirectoryDescriptor> {
        return this.http.post(resolver.directoryCreationUri(), creationRequest)
            .map(res => {
                this.notifyer.showSuccess('Le répertoire a bien été créé');
                return DirectoryDescriptor.dfFromRaw(res.json());
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la création du répertoire');
                return Observable.of(new DirectoryDescriptor());
            });
    }

    /**
     * Supprime les fichiers dans le répertoire courant.
     */
    public deleteFiles(parentDirectory: DirectoryDescriptor,
        filesToDelete: Array<FileDescriptor>,
        resolver: FileBrowserResolver): Observable<DirectoryDescriptor> {
        const responses = new Array<Observable<Response>>();

        // préparation des x appels de suppression
        filesToDelete.forEach(fileToDelete => {
            responses.push(this.http.delete(resolver.fileDeletionUri(fileToDelete)));
        });


        // lancement des x appels en paralleles, avec attente de synchro pour la récupération finale des résultats.
        return Observable.forkJoin(responses)
            .map(res => {
                // conversion de l'ensemble des résultats
                return JsonTools.map(res.map(oneRes => oneRes.json()), FileDeletionResult.fromRaw);
            })
            .flatMap(res => {

                // contage des suppression effectives.
                const nbDeleted = res.filter(oneResult => oneResult.deleted).length;

                if (nbDeleted === 0) {
                    this.notifyer.showError('Toutes les suppressions sont ko');
                }

                if (nbDeleted === filesToDelete.length) {
                    this.notifyer.showSuccess('Toutes les suppressions sont ok');
                } else if (nbDeleted > 0) {
                    this.notifyer.showInfo('Certaines suppressions sont ko');
                }


                return this.getDirectoryDetail(parentDirectory, resolver);
            });

    }
}
