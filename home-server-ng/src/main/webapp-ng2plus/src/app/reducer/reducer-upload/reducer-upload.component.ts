import { NotifyerService } from './../../common-gui/notifyer/notifyer.service';
import { ReducedImage } from './../models/reduced-image.modele';
import { ReducerService } from './../reducer.service';
import { ImportedFiles } from './../../common-gui/import-file-button/imported-files.modele';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-reducer-upload',
    templateUrl: 'reducer-upload.component.html',
    styleUrls: ['reducer-upload.component.scss']
})
export class ReducerUploadComponent implements OnInit {


    @Output()
    public imageListUpdated = new EventEmitter<Array<ReducedImage>>();

    constructor(private reducerService: ReducerService, private notifyer: NotifyerService) { }



    ngOnInit() { }

    public startUploadFile(file: ImportedFiles) {

        this.notifyer.waitingInfo('Import des images en cours...');

        this.reducerService.remoteReduceImage(file).subscribe(res => {
            this.imageListUpdated.emit(res);
            this.notifyer.hide();
        });
    }
}