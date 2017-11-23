import { FileBrowserResolver } from './../file-browser-resolver.modele';
import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { Component, OnInit, ViewChild } from '@angular/core';
import { DirectoryDescriptor } from 'app/shared/directory-descriptor.modele';
import { PopupComponent } from 'app/common-gui/popup/popup.component';

@Component({
    selector: 'homeserver-file-browser-download',
    templateUrl: 'file-browser-download.component.html'
})

export class FileBrowserDownloadComponent implements OnInit {

    /**
             * Popup pour le renommage des fichiers.
             * @type {PopupComponent}
             * @memberOf FileBrowserComponent
             */
    @ViewChild('popupDownloadFiles')
    popupRenameFiles: PopupComponent;

    public directoryList: Array<FileDescriptor>;
    public fileList: Array<FileDescriptor>;
    public resolver: FileBrowserResolver;


    constructor() { }

    ngOnInit() { }

    public display(resolver: FileBrowserResolver, directoryList: Array<FileDescriptor>, fileList: Array<FileDescriptor>): void {
        this.resolver = resolver;
        this.directoryList = directoryList;
        this.fileList = fileList;
        this.popupRenameFiles.display();
    }

    public innerClose(): void {

    }
}
