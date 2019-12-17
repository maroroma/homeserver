import { StepsBuilder } from './../common-gui/steps-bar/steps-builder.service';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'homeserver-music',
    templateUrl: 'music.component.html'
})

export class MusicComponent implements OnInit {

    public steps = new StepsBuilder()
        .addStep('Archivables')
        .addStep('Album')
        .addStep('AlbumArt')
        .addStep('Tracks!')
        .addStep('Tags')
        .build();

    constructor() { }


    ngOnInit() { }
}
