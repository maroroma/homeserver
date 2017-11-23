import { FileBrowserOptionsBuilder } from './../common-gui/file-browser/file-browser-options-builder.modele';
import { FileManagerResolver } from './filemanager-resolver.modele';
import { DirectoryDescriptor } from './../shared/directory-descriptor.modele';
import { FileManagerService } from './filemanager.service';
import { Component, OnInit } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-filemanager',
    templateUrl: 'filemanager.component.html',
    styleUrls: ['filemanager.component.scss']
})
export class FileManagerComponent implements OnInit {

    public starterMockDirectory: DirectoryDescriptor;

    public fileBrowserOptions = FileBrowserOptionsBuilder
        .prepare()
        .deletion()
        .directoryCreation()
        .refresh()
        .selection()
        .renaming()
        .downloading()
        .withResolver(new FileManagerResolver()).toolbar().build();



    constructor(private fileManagerService: FileManagerService) { }

    ngOnInit() {
        this.fileManagerService.getRootDirectories().subscribe(res => {
            this.starterMockDirectory = new DirectoryDescriptor();
            this.starterMockDirectory.name = 'Emplacements';
            this.starterMockDirectory.directories = res.map(target => {
                const mapped = new DirectoryDescriptor();
                mapped.fullName = target.fullName;
                mapped.id = target.id;
                mapped.name = target.name;
                return mapped;
            });
        });

    }
}
