import { Component, Output, EventEmitter } from '@angular/core';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Component({
    selector: 'homeserver-select-tracks',
    templateUrl: 'select-tracks.components.html',
})
export class SelectTracksComponent {

    @Output()
    public goToNextStep = new EventEmitter<any>();

    public uploadTracksMode = false;
    public serverTracksMode = false;

    constructor() {

    }

    public selectUploadTracksMode(): void {
        this.uploadTracksMode = true;
        this.serverTracksMode = false;
    }
    public selectServerTracksMode(): void {
        this.uploadTracksMode = false;
        this.serverTracksMode = true;
    }


    public tracksSelected(): void {
        this.goToNextStep.emit();
    }
}