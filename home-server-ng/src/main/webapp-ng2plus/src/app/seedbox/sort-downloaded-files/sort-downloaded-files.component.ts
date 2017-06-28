import { StepsBarComponent } from './../../common-gui/steps-bar/steps-bar.component';
import { StepsBuilder } from './../../common-gui/steps-bar/steps-builder.service';
import { SortDownloadedFilesService } from './sort-downloaded-files.service';
import { Component, OnInit, Input, OnChanges, ViewChild } from '@angular/core';
import {
    trigger,
    state,
    style,
    animate,
    transition, keyframes
} from '@angular/animations';

@Component({
    moduleId: module.id,
    selector: 'homeserver-sort-downloaded-files',
    templateUrl: 'sort-downloaded-files.component.html'
})
export class SortDownloadedFilesComponent implements OnInit, OnChanges {

    @ViewChild('stepsBar')
    public stepsBar: StepsBarComponent;

    public state = 'in';

    public steps = new StepsBuilder()
        .addStep('Choisir les fichiers')
        .addStep('Choisir la cible')
        .addStep('Confirmer').build();

    constructor() { }

    ngOnInit() { }

    ngOnChanges() {
    }
}