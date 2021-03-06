import { ReflectionTools } from './../../shared/reflection-tools.service';
import { Injectable } from '@angular/core';
import { TimerObservable } from 'rxjs/observable/TimerObservable';
import { SimpleProgressEvent } from '../../shared/simple-progress-event.modele';


/**
 * Service permettant de piloter l'affichage des notifications.
 * Permet de piloter aussi les animations et le temps d'affichage des informations.
 * @export
 * @class NotifyerService
 */
@Injectable()
export class NotifyerService {

    currentState = 'hide';
    defaultTimeOut = 1500;
    message: string;
    detail: string;
    lastProgress: SimpleProgressEvent;
    forInfo = false;
    forSuccess = false;
    forError = false;
    forProgress = false;
    blocking = false;

    constructor() {

    }

    public hide(): void {
        this.currentState = 'hide';
        this.blocking = false;
    }

    public display(message: string, detail?: string, timeout?: number): void {
        this.message = message;
        this.currentState = 'display';
        this.detail = detail;
        const displayTime = timeout ? timeout : this.defaultTimeOut;
        const subscription = TimerObservable.create(displayTime, displayTime).subscribe(t => {
            this.hide();
            subscription.unsubscribe();
        });
    }

    public showInfo(message: string, timeout?: number): void {
        this.reinitStatusExcept('forInfo');
        this.display(message, null, timeout);
    }

    public showSuccess(message: string, timeout?: number): void {
        this.reinitStatusExcept('forSuccess');
        this.display(message, null, timeout);
    }

    public showError(message: string, detail?: string, timeout?: number): void {
        this.reinitStatusExcept('forError');
        this.display(message, detail, timeout);
    }

    public successfullSave(): void {
        this.showSuccess('Sauvegarde effectuée.');
    }

    public successfullDelete(): void {
        this.showSuccess('Suppression effectuée.');
    }

    public successfullUpload(file?: File): void {
        let msg = 'Upload du fichier';
        if (file) {
            msg = msg + file.name;
        }
        msg = msg + ' terminé';
        this.showSuccess(msg);
    }

    public waitingInfo(message: string, detail?: string) {
        this.reinitStatusExcept('forInfo', 'blocking');
        this.currentState = 'display';
        this.message = message;
        this.detail = detail;
    }

    public showProgress(message: string, progress: SimpleProgressEvent) {
        this.reinitStatusExcept('forInfo', 'forProgress');
        this.lastProgress = progress;
        this.currentState = 'display';
        this.message = message;
    }

    private reinitStatus(): void {
        this.forInfo = false;
        this.forError = false;
        this.forSuccess = false;
        this.blocking = false;
        this.forProgress = false;
    }

    private reinitStatusExcept(...exceptions: string[]): void {
        this.reinitStatus();
        exceptions.forEach(fieldName => ReflectionTools.writeData(this, fieldName, true));
    }




}
