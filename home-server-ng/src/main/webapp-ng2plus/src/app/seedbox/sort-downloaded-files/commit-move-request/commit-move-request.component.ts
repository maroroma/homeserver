import { PopupComponent } from './../../../common-gui/popup/popup.component';
import { MovedFile } from './../models/moved-file.modele';
import { MoveRequestBuilder } from './../models/move-request-builder.modele';
import { SortDownloadedFilesService } from './../sort-downloaded-files.service';
import { Component, OnInit, Output, EventEmitter, ViewChild } from '@angular/core';

@Component({
    selector: 'homeserver-commit-move-request',
    templateUrl: 'commit-move-request.component.html',
    styleUrls: ['../../../common-gui/styles/edit-zone.scss', '../completed-file-list/completed-file-list.component.scss']
})
export class CommitMoveRequestComponent implements OnInit {

    public moveRequest: MoveRequestBuilder;

    @Output()
    public returnToTarget = new EventEmitter<any>();
    @Output()
    public returnToFiles = new EventEmitter<any>();

    @ViewChild('popupConfirmation')
    popupConfirmation: PopupComponent;

    public result: Array<MovedFile>;

    constructor(private sortService: SortDownloadedFilesService) { }

    ngOnInit() {
        this.moveRequest = this.sortService.moveRequestBuilder;
    }

    public innerGotoSelectTarget(): void {
        this.returnToTarget.emit();
    }

    public innerGotoSelectFiles(): void {
        this.returnToFiles.emit();
    }

    public commit(): void {
        this.sortService.commitMove().subscribe(res => {
            this.result = res;
            this.popupConfirmation.display();
        });
    }
}

