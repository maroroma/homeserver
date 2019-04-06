import { Component, ViewChild, Output, EventEmitter } from '@angular/core';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { PopupComponent } from '../../popup/popup.component';
import { ImportedFiles } from '../../import-file-button/imported-files.modele';
import { VisualItemDataSource } from '../../../shared/visual-item-datasource.modele';
import { VisualItem } from '../../../shared/visual-item.modele';
import { FilterTools } from '../../../shared/filter-tools.service';

@Component({
    selector: 'homeserver-file-browser-upload',
    templateUrl: 'file-browser-upload.component.html',
    styleUrls: ['file-browser-upload.component.scss']
})
export class FileBrowserUploadComponent {

    @ViewChild('popupUploadFiles')
    popupUploadFiles: PopupComponent;

    @Output()
    confirmUpload = new EventEmitter<ImportedFiles>();

    uploadFileList = new VisualItemDataSource<File>();

    constructor() {

    }

    public display(): void {
        this.popupUploadFiles.display();
    }

    /**
     * Déclenche l'upload
     * @param file 
     */
    public addFilesToUpload(files: ImportedFiles): void {
        this.uploadFileList.addToSourceList(files.files);
    }

    public removeFileToUpload(file: VisualItem<File>): void {
        this.uploadFileList.updateSourceList(
            FilterTools.removeAny(this.uploadFileList.getRawItemsFromSource(), oneFile => oneFile.name === file.item.name)
        );
    }

    public innerOk() : void {
        const filesToUpload = new ImportedFiles(this.uploadFileList.getRawItemsFromSource());
        this.uploadFileList.clearSourceList();
        this.confirmUpload.emit(filesToUpload);
    }

    public innerKo():void {
        this.uploadFileList.clearSourceList();    
    }
}