import { SendMailRequest } from './models/send-mail-request.modele';
import { ContactDescriptor } from './models/contact-descriptor.modele';
import { JsonTools } from './../shared/json-tools.service';
import { ApiConstants } from './../shared/api-constants.modele';
import { ReducedImage } from './models/reduced-image.modele';
import { Observable } from 'rxjs/Rx';
import { Http, Response } from '@angular/http';
import { NotifyerService } from './../common-gui/notifyer/notifyer.service';
import { Injectable } from '@angular/core';

@Injectable()
export class ReducerService {

    constructor(private http: Http, private notifyer: NotifyerService) { }

    public loadReducedImages(): Observable<Array<ReducedImage>> {
        return this.http.get(ApiConstants.REDUCER_REDUCED_IMAGES_API)
            .map(res => {
                return JsonTools.map(res.json(), ReducedImage.fromRaw);
            }).catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la récupération des images réduites');
                return Observable.of(new Array<ReducedImage>());
            });
    }

    public deleteReducedImages(imagesToDelete: Array<ReducedImage>): Observable<Array<ReducedImage>> {

        // génération des x appels
        const responsesPromises = imagesToDelete
            .map(oneImage => this.http.delete(ApiConstants.REDUCER_REDUCED_IMAGE_API + '/' + oneImage.id));


        return Observable.forkJoin(responsesPromises).flatMap(res => {
            this.notifyer.showSuccess('Les fichiers ont bien été supprimés');
            return this.loadReducedImages();
        }).catch((err, data) => {
            this.notifyer.showError('Une erreur est survenue lors de la suppression des images réduites');
            return Observable.of(new Array<ReducedImage>());
        });


    }

    public remoteReduceImage(fileList: FileList): Observable<Array<ReducedImage>> {
        // génération des x appels

        const responsesPromises = new Array<Observable<Response>>();


        for (let i = 0; i < fileList.length; i++) {
            const fd = new FormData();
            fd.append('imageToReduce', fileList[i]);
            responsesPromises.push(this.http.post(ApiConstants.REDUCER_REDUCED_FULL_SIZE_IMAGE_API, fd));
        }


        return Observable.forkJoin(responsesPromises).flatMap(res => {
            this.notifyer.showSuccess('Toutes les images ont bien été réduites.');
            return this.loadReducedImages();
        }).catch((err, data) => {
            this.notifyer.showError('Une erreur est survenue lors de la réduction des images');
            return Observable.of(new Array<ReducedImage>());
        });
    }

    public searchContact(contact: string): Observable<Array<ContactDescriptor>> {
        return this.http.get(ApiConstants.REDUCER_REDUCED_FIND_CONTACTS_API + '/' + contact)
            .map(res => JsonTools.map(res.json(), ContactDescriptor.fromRawJson))
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de la recherche des contacts');
                return Observable.of(new Array<ContactDescriptor>());
            });
    }

    public sendMail(sendMailRequest: SendMailRequest): Observable<boolean> {
        return this.http.post(ApiConstants.REDUCER_REDUCED_SEND_MAIL_API, sendMailRequest)
            .map(res => true)
            .catch((err, data) => {
                this.notifyer.showError('Une erreur est survenue lors de l\'émission du mail');
                return Observable.of(false);
            });
    }

}
