import { RenameFileDescriptor } from './rename-file-descriptor.modele';
import { FileOperationResult } from './file-operation-result.modele';
import { ApiConstants } from './../../shared/api-constants.modele';
import { FileDescriptor } from './../../shared/file-descriptor.modele';
import { NotifyerService } from './../notifyer/notifyer.service';
import { DirectoryCreationRequest } from './directory-creation-request.modele';
import { JsonTools } from './../../shared/json-tools.service';
import { FileBrowserResolver } from './file-browser-resolver.modele';
import { Observable } from 'rxjs/Rx';
import { DirectoryDescriptor } from './../../shared/directory-descriptor.modele';
import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';
import { ImportedFiles } from '../import-file-button/imported-files.modele';
import { HttpRequest } from 'selenium-webdriver/http';
import { ChainedHttpCalls } from '../../shared/chained-http-calls.service';

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
     * @memberOf FileBrowserService
     */
    public getDirectoryDetail(directory: DirectoryDescriptor, resolver: FileBrowserResolver): Observable<DirectoryDescriptor> {
        return this.http.get(resolver.directoryDetailsUri(directory))
            .map(res => {
                return DirectoryDescriptor.dfFromRaw(res.json());
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération du répertoire');
                return Observable.of(new DirectoryDescriptor());
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
     * upload d'un fichier
     * @param targetDirectory répertoire cible de l'upload 
     * @param fileList liste des fichiers à uploader
     * @param resolver résolver
     */
    public uploadFile(targetDirectory: DirectoryDescriptor, fileList: ImportedFiles, resolver: FileBrowserResolver): Observable<DirectoryDescriptor> {

        this.notifyer.waitingInfo('upload en cours...');

        return this.http.post(resolver.fileUploadUri(targetDirectory), fileList.mapToFormData())
            .map(res => {
                this.notifyer.showSuccess('file uploadé rajouté');
                return new DirectoryDescriptor();
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de l\'upload du fichier', err.json().message);
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
                return JsonTools.map(res.map(oneRes => oneRes.json()), FileOperationResult.fromRaw);
            })
            .flatMap(res => {

                // contage des suppression effectives.
                const nbDeleted = res.filter(oneResult => oneResult.completed).length;

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

    /*
     * Permet de renommer un ensemble de fichier.
     * @param {DirectoryDescriptor} parentDirectory
     * @param {Array<RenameFileDescriptor>} filesToRename
     * @param {FileBrowserResolver} resolver
     * @returns {Observable<DirectoryDescriptor>}
     * @memberof FileBrowserService
    */
    public renameFiles(parentDirectory: DirectoryDescriptor,
        filesToRename: Array<RenameFileDescriptor>,
        resolver: FileBrowserResolver): Observable<DirectoryDescriptor> {

        const responses = new Array<Observable<Response>>();

        // préparation des x appels de renomage
        filesToRename.forEach(oneFileToRename => {
            responses.push(this.http.patch(resolver.fileRenameUri(), oneFileToRename));
        });


        // lancement des x appels en paralleles, avec attente de synchro pour la récupération finale des résultats.
        return Observable.forkJoin(responses)
            .map(res => {
                // conversion de l'ensemble des résultats
                return JsonTools.map(res.map(oneRes => oneRes.json()), FileOperationResult.fromRaw);
            })
            .flatMap(res => {

                // contage des renommage effectives.
                const nbRenamed = res.filter(oneResult => oneResult.completed).length;

                if (nbRenamed === 0) {
                    this.notifyer.showError('Toutes les renommages sont ko');
                }

                if (nbRenamed === filesToRename.length) {
                    this.notifyer.showSuccess('Toutes les renommages sont ok');
                } else if (nbRenamed > 0) {
                    this.notifyer.showInfo('Certaines renommages sont ko');
                }


                return this.getDirectoryDetail(parentDirectory, resolver);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors du renommage des fichiers');
                return Observable.of(new DirectoryDescriptor());
            });
    }
}
