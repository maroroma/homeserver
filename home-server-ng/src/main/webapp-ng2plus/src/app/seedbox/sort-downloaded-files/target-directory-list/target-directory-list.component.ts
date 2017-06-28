import { FileBrowserOptionsBuilder } from './../../../common-gui/file-browser/file-browser-options-builder.modele';
import { TargetDirectoryResolver } from './target-directory-resolver.modele';
import { NotifyerService } from './../../../common-gui/notifyer/notifyer.service';
import { SortDownloadedFilesService } from './../sort-downloaded-files.service';
import { DirectoryDescriptor } from './../../../shared/directory-descriptor.modele';

import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';

@Component({
    moduleId: module.id,
    selector: 'homeserver-target-directory-list',
    templateUrl: 'target-directory-list.component.html',
    styleUrls: ['../../../common-gui/styles/edit-zone.scss']
})
export class TargetDirectoryListComponent implements OnInit {

    public starterMockDirectory: DirectoryDescriptor;
    private selectedTarget: DirectoryDescriptor;

    public fileBrowserOptions = FileBrowserOptionsBuilder.prepare()
        .directoryCreation()
        .withResolver(new TargetDirectoryResolver()).build();


    @Output()
    public stepBack = new EventEmitter<any>();

    @Output()
    public goToNextStep = new EventEmitter<any>();

    public isTargetValid = false;

    constructor(private sortService: SortDownloadedFilesService, private notifyer: NotifyerService) { }


    ngOnInit() {
        this.sortService.loadTargets().subscribe(res => {
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

    public innnerStepBack(): void {
        this.stepBack.emit();
    }

    public onSelectedDirectoryChange(selectedDirectory: DirectoryDescriptor): void {
        this.isTargetValid = this.starterMockDirectory.id !== selectedDirectory.id;
        if (this.isTargetValid) {
            this.selectedTarget = selectedDirectory;
        }
    }

    public gotoMoveAction(): void {
        this.sortService.moveRequestBuilder.targetDirectory = this.selectedTarget;
        this.goToNextStep.emit();
    }
}
