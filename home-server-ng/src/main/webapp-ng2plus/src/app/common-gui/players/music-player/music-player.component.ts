import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { OnDestroy } from '@angular/core/src/metadata/lifecycle_hooks';
import { AbstractPlayer } from 'app/common-gui/players/abstract-player.component';
@Component({
    selector: 'homeserver-music-player',
    templateUrl: 'music-player.component.html',
    styleUrls: ['../abstract-player.component.scss']
})

export class MusicPlayerComponent extends AbstractPlayer {

    // @Input()
    // public fileToPlay: FileDescriptor;

    // @ViewChild('audioPlayer') audioPlayer: ElementRef;

    constructor() {
        super()
    }

    // ngOnInit() { }

    // ngOnDestroy() {
    //     console.log('musicPlayer ondestroy', this.audioPlayer);
    //     this.stop();
    // }

    // public stop(): void {
    //     if (this.audioPlayer && this.audioPlayer.nativeElement) { this.audioPlayer.nativeElement.pause(); }
    // }
}
