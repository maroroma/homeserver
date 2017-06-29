import { FileBrowserOptions } from './file-browser-options.modele';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
    selector: 'homeserver-file-browser-toolbar',
    templateUrl: 'file-browser-toolbar.component.html'
})

export class FileBrowserToolBarComponent implements OnInit {

    @Input()
    public options: FileBrowserOptions;

    @Input()
    public isFilesSelected = false;
    @Input()
    public isCurrentDirectoryRoot = false;

    @Input()
    public nbFiles = 0;
    @Input()
    public nbDirectories = 0;

    @Output()
    public refreshCurrentDirectory = new EventEmitter<any>();
    @Output()
    public displayDirectoryCreation = new EventEmitter<any>();
    @Output()
    public displayDirectoryDeletionConfirmation = new EventEmitter<any>();

    constructor() { }
    ngOnInit() { }

    public innerRefreshCurrentDirectory():void {
        this.refreshCurrentDirectory.emit();
    }

    public innerDisplayDirectoryCreation():void {
        this.displayDirectoryCreation.emit();

    }

    public innerDisplayDirectoryDeleteConfirmation() :void {
        this.displayDirectoryDeletionConfirmation.emit();
    }
}
