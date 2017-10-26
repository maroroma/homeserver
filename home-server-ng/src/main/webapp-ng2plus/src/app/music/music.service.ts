import { TrackDescriptor } from './models/track-descriptor.modele';
import { Observable } from 'rxjs/Observable';
import { JsonTools } from './../shared/json-tools.service';
import { ApiConstants } from './../shared/api-constants.modele';
import { AlbumDescriptor } from './models/album-descriptor.modele';
import { NotifyerService } from './../common-gui/notifyer/notifyer.service';
import { Http, Response } from '@angular/http';
import { Injectable } from '@angular/core';

@Injectable()
export class MusicService {
    public currentAlbumDescriptor: AlbumDescriptor;

    constructor(private http: Http, private notifyer: NotifyerService) { }



    /**
     * Création du répertoire de travail
     * @param albumDescriptor 
     */
    public prepareWorkingDirectory(albumDescriptor: AlbumDescriptor): Observable<AlbumDescriptor> {
        this.currentAlbumDescriptor = null;

        return this.http.post(ApiConstants.MUSIC_WORKING_DIR_API, albumDescriptor)
            .map(res => {
                this.notifyer.showSuccess('Répertoire de travail créé');

                this.currentAlbumDescriptor = AlbumDescriptor.dfFromRaw(res.json());
                return this.currentAlbumDescriptor;
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la création du répertoire de travail');
                return Observable.of(null);
            });

    }

    /**
     * Ajout d'un album art
     * @param {FileList} fileList 
     * @returns {Observable<AlbumDescriptor>} 
     * @memberof MusicService
     */
    public addAlbumArt(fileList: FileList): Observable<AlbumDescriptor> {
        // génération des x appels

        const fd = new FormData();
        fd.append('albumart', fileList[0]);
        return this.http.patch(ApiConstants.MUSIC_WORKING_DIR_API + '/'
            + this.currentAlbumDescriptor.directoryDescriptor.id
            + '/albumart/', fd).map(res => {
                this.notifyer.showSuccess('albumart rajouté');

                this.currentAlbumDescriptor = AlbumDescriptor.dfFromRaw(res.json());
                return this.currentAlbumDescriptor;
            })
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors l\'ajout de l\'albumart');
                return Observable.of(null);
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
     * Emission des tracks vers le serveur.
     * Appel multiple en //, avec retour contenant la liste finale des fichiers.
     * @param {Array<File>} fileList -
     * @returns {Observable<Array<TrackDescriptor>>} -
     * @memberof MusicService
     */
    public sendTracks(fileList: Array<File>): Observable<Array<TrackDescriptor>> {
        // génération des x appels

        const responsesPromises = fileList.map(oneFile => {
            const fd = new FormData();
            fd.append('track', oneFile);
            return this.http.post(ApiConstants.MUSIC_WORKING_DIR_API + '/'
                + this.currentAlbumDescriptor.directoryDescriptor.id
                + '/tracks', fd);
        });

        return Observable.forkJoin(responsesPromises).flatMap(res => {
            this.notifyer.showSuccess('Tous les fichiers ont bien été uploadés');
            return this.getTracks();
        }).catch((err, data) => {
            this.notifyer.showError('Une erreur est survenue lors de l\'upload des fichiers');
            return Observable.of(new Array<TrackDescriptor>());
        });
    }

    /**
     * Update des tracks vers le serveur.
     * Appel multiple en //, avec retour contenant la liste finale des fichiers.
     * @param {Array<File>} fileList -
     * @returns {Observable<Array<TrackDescriptor>>} -
     * @memberof MusicService
     */
    public updateTracks(trackList: Array<TrackDescriptor>): Observable<Array<TrackDescriptor>> {
        // génération des x appels

        const responsesPromises = trackList.map(oneFile => {
            return this.http.patch(ApiConstants.MUSIC_WORKING_DIR_API + '/'
                + this.currentAlbumDescriptor.directoryDescriptor.id
                + '/tracks', oneFile);
        });

        return Observable.forkJoin(responsesPromises).flatMap(res => {
            this.notifyer.showSuccess('Tous les fichiers ont bien été mis à jour');
            return this.getTracks();
        }).catch((err, data) => {
            this.notifyer.showError('Une erreur est survenue lors de la mise à jour des fichiers');
            return Observable.of(new Array<TrackDescriptor>());
        });
    }
}
