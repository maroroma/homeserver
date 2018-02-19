import { Component, OnInit, Input, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { FileDescriptor } from 'app/shared/file-descriptor.modele';
import { AbstractPlayer } from 'app/common-gui/players/abstract-player.component';

@Component({
    selector: 'homeserver-video-player',
    templateUrl: 'video-player.component.html',
    styleUrls: ['../abstract-player.component.scss']
})

export class VideoPlayerComponent extends AbstractPlayer {

    // @Input()
    // public fileToPlay: FileDescriptor;

    // @ViewChild('videoPlayer') videoPlayer: ElementRef;

    constructor() {
        super();
    }

    // ngOnInit() { }

    // ngOnDestroy() {
    //     console.log('videoPlayer ondestroy', this.videoPlayer);
    //     this.stop();
    // }

    // public stop(): void {

    //     if (this.videoPlayer && this.videoPlayer.nativeElement) {
    //         this.videoPlayer.nativeElement.pause();
    //     }
    // }
}
