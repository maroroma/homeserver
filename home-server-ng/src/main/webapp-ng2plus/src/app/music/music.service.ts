import { Subject } from 'rxjs/Subject';
import { ChainedHttpCalls } from './../shared/chained-http-calls.service';
import { TrackDescriptor } from './models/track-descriptor.modele';
import { Observable } from 'rxjs/Observable';
import { JsonTools } from './../shared/json-tools.service';
import { ApiConstants } from './../shared/api-constants.modele';
import { AlbumDescriptor } from './models/album-descriptor.modele';
import { NotifyerService } from './../common-gui/notifyer/notifyer.service';
import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';
import { ImportedFiles } from '../common-gui/import-file-button/imported-files.modele';
import { FileDescriptor } from '../shared/file-descriptor.modele';
import { AddTracksFromExistingSourceRequest } from './models/add-tracks-from-existing-source-request.modele';

@Injectable()
export class MusicService {
    public currentAlbumDescriptor: AlbumDescriptor;

    constructor(private http: Http, private notifyer: NotifyerService) { }



    /**
     * Création du répertoire de travail
     * @param albumDescriptor
     */
    public prepareWorkingDirectory(albumDescriptor: AlbumDescriptor): Observable<AlbumDescriptor | any> {
        this.currentAlbumDescriptor = null;

        return this.http.post(ApiConstants.MUSIC_WORKING_DIR_API, albumDescriptor)
            .map(res => {
                this.notifyer.showSuccess('Répertoire de travail créé');

                this.currentAlbumDescriptor = AlbumDescriptor.dfFromRaw(res.json());

                return this.currentAlbumDescriptor;
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la création du répertoire de travail', err.json().message);
                throw new Error(err.json().message);
            });

    }

    /**
     * Fin de traitement, on réinitialise l'album descriptor.
     */
    public finish(): void {
        this.currentAlbumDescriptor = null;
    }

    /**
     * Ajout d'un album art
     * @param {FileList} fileList
     * @returns {Observable<AlbumDescriptor>}
     * @memberof MusicService
     */
    public addAlbumArt(fileList: ImportedFiles): Observable<AlbumDescriptor | {}> {
        // génération des x appels

        const fd = fileList.mapUniqueFileToFormData('albumart');
        return this.http.patch(ApiConstants.MUSIC_WORKING_DIR_API + '/'
            + this.currentAlbumDescriptor.directoryDescriptor.id
            + '/albumart/', fd)
            .map(res => {
                this.notifyer.showSuccess('albumart rajouté');

                this.currentAlbumDescriptor = AlbumDescriptor.dfFromRaw(res.json());
                return this.currentAlbumDescriptor;
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors l\'ajout de l\'albumart', err.json().message);
                throw new Error(err.json().message);
            });



    }

    public getTracks(): Observable<Array<TrackDescriptor>> {
        return this.http.get(ApiConstants.MUSIC_WORKING_DIR_API + '/'
            + this.currentAlbumDescriptor.directoryDescriptor.id
            + '/tracks').map(res => {
                return JsonTools.map(res.json(), TrackDescriptor.dfFromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des morceaux');
                return Observable.of(null);
            });
    }

    /**
     * R2cupère la liste des fichiers mp3 présents sur le serveur et prêts à etre triés
     */
    public getAvailableTracks(): Observable<Array<FileDescriptor>> {
        return this.http.get(ApiConstants.MUSIC_AVAILABLE_FILES_API)
            .map(res => {
                return JsonTools.map(res.json(), FileDescriptor.fromRaw);
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des morceaux existants');
                return Observable.of(null);
            });
    }

    public copyAvailableTracksSelection(selectedFiles: Array<FileDescriptor>): Observable<Array<TrackDescriptor>> {
        return this.http.post(ApiConstants.MUSIC_WORKING_DIR_API + '/'
            + this.currentAlbumDescriptor.directoryDescriptor.id + '/existingtracks',
            new AddTracksFromExistingSourceRequest(selectedFiles.map(oneFile => oneFile.id)))
            .map(res => JsonTools.map(res.json(), TrackDescriptor.dfFromRaw))
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des morceaux existants');
                return Observable.of(null);
            });
    }


    public getCompletedAlbums(): Observable<Array<AlbumDescriptor>> {
        return this.http.get(ApiConstants.MUSIC_WORKING_DIR_API)
            .map(res => JsonTools.map(res.json(), AlbumDescriptor.dfFromRaw))
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des album déjà traités');
                return Observable.of(null);
            });
    }

    /**
     * Emission des tracks vers le serveur.
     * Appel multiple en //, avec retour contenant la liste finale des fichiers.
     * @param {Array<File>} fileList -
     * @returns {Observable<Array<TrackDescriptor>>} -
     * @memberof MusicService
     */
    public sendTracks(fileList: Array<File>): ChainedHttpCalls<TrackDescriptor, File> {
        // public sendTracks(fileList: Array<File>): Subject<Array<TrackDescriptor>> {

        const chaineCall = new ChainedHttpCalls<TrackDescriptor, File>(fileList);

        // const returnValue =
        chaineCall.submit(oneFile => {
            const fd = new FormData();
            fd.append('track', oneFile);
            return this.http.post(ApiConstants.MUSIC_WORKING_DIR_API + '/'
                + this.currentAlbumDescriptor.directoryDescriptor.id
                + '/tracks', fd);
        }, response => TrackDescriptor.dfFromRaw(response.json()));


        return chaineCall;

        // returnValue.subscribe(res => this.notifyer.showSuccess('Tous les fichiers ont bien été uploadés'));

        // return returnValue;


    }

    /**
     * Update des tracks vers le serveur.
     * Appel multiple en //, avec retour contenant la liste finale des fichiers.
     * @param {Array<File>} fileList -
     * @returns {Observable<Array<TrackDescriptor>>} -
     * @memberof MusicService
     */
    public updateTracks(trackList: Array<TrackDescriptor>): Observable<AlbumDescriptor> {
        // génération des x appels

        const responsesPromises = trackList.map(oneFile => {
            return this.http.patch(ApiConstants.MUSIC_WORKING_DIR_API + '/'
                + this.currentAlbumDescriptor.directoryDescriptor.id
                + '/tracks', oneFile);
        });

        return Observable.forkJoin(responsesPromises)
            .flatMap(res => {
                this.notifyer.showSuccess('Tous les fichiers ont bien été mis à jour');
                return this.completeAlbum();
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la mise à jour des fichiers');
                return Observable.of(new AlbumDescriptor());
            });
    }

    /**
     * Marque l'album comme étant complété
     */
    public completeAlbum(): Observable<AlbumDescriptor> {
        return this.http.patch(ApiConstants.MUSIC_WORKING_DIR_API + '/'
            + this.currentAlbumDescriptor.directoryDescriptor.id
            + '/complete', {})
            .map(res => {
                return AlbumDescriptor.dfFromRaw(res.json())
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la complétion de l\'album');
                return Observable.of(null);
            });

    }

    /**
     * Marque l'album comme étant complété, et le recopie dans le répertoire cible
     */
    public archiveAlbum(albumDescriptor: AlbumDescriptor): Observable<AlbumDescriptor> {
        return this.http.post(ApiConstants.MUSIC_WORKING_DIR_API + '/'
            + albumDescriptor.directoryDescriptor.id
            + '/archive', {})
            .map(res => {
                return AlbumDescriptor.dfFromRaw(res.json())
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de l\'archivage de l\'album');
                return Observable.of(null);
            });

    }
}
