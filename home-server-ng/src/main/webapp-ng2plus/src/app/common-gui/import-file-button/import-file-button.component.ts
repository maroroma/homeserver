import { ImportedFiles } from './imported-files.modele';
import { Component, OnInit, ViewChild, Input, EventEmitter, Output } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-import-file-button',
    templateUrl: 'import-file-button.component.html',
    styleUrls: ['import-file-button.component.scss']
})
export class ImportFileButtonComponent implements OnInit {

    @Input()
    associatedItem: any;

    @Input()
    isMultiple = false;

    @Output()
    fileChanged = new EventEmitter<ImportedFiles>();

    constructor() { }


    ngOnInit() {
    }

    public innerChange(event: Event): any {
        const returnValue = new ImportedFiles();
        returnValue.associatedItem = this.associatedItem;
        returnValue.files = (event.target as HTMLInputElement).files;
        this.fileChanged.emit(returnValue);
        (event.target as HTMLInputElement).value = '';
    }
}
