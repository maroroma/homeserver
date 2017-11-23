import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { PopupComponent } from 'app/common-gui/popup/popup.component';
import { Component, OnInit, Input, ViewChild, EventEmitter, Output } from '@angular/core';
import { RenameFileDescriptor } from 'app/common-gui/file-browser/rename-file-descriptor.modele';

@Component({
    selector: 'homeserver-file-browser-rename',
    templateUrl: 'file-browser-rename.component.html'
})

export class FileBrowserRenameComponent implements OnInit {

    public filesToRename: Array<RenameFileDescriptor>;

    /**
         * Popup pour le renommage des fichiers.
         * @type {PopupComponent}
         * @memberOf FileBrowserComponent
         */
    @ViewChild('popupRenameFiles')
    popupRenameFiles: PopupComponent;

    @Output()
    confirmRename = new EventEmitter<Array<RenameFileDescriptor>>();

    constructor() { }

    ngOnInit() { }

    /**
     * Affichage de la popup interne sur la liste en entrée
     * @param fileToRenames
     */
    public display(fileToRenames: Array<FileDescriptor>): void {
        this.filesToRename = fileToRenames.map(fd => new RenameFileDescriptor(fd));
        this.popupRenameFiles.display();
    }

    public innerConfirmRename(): void {
        this.confirmRename.emit(this.filesToRename);
    }
}
