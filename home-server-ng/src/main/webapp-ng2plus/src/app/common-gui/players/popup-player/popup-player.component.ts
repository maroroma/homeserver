import { VideoPlayerComponent } from './../video-player/video-player.component';
import { PopupComponent } from './../../popup/popup.component';
import { FileDescriptor } from './../../../shared/file-descriptor.modele';
import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractPlayer } from 'app/common-gui/players/abstract-player.component';
import { MusicPlayerComponent } from 'app/common-gui/players/music-player/music-player.component';

@Component({
    selector: 'homeserver-popup-player',
    templateUrl: 'popup-player.component.html'
})

export class PopupPlayerComponent implements OnInit {

    public popupTitle: string;
    public isMusicPlayer: boolean;
    public fileToPlay: FileDescriptor;

    @ViewChild('popup')
    popup: PopupComponent;
    @ViewChild('musicPlayer')
    musicPlayer: MusicPlayerComponent;
    @ViewChild('videoPlayer')
    videoPlayer: VideoPlayerComponent;


    constructor() { }

    ngOnInit() { }

    public displayForVideo(file: FileDescriptor): void {
        this.display(false, file);
    }

    public displayForAudio(file: FileDescriptor): void {
        this.display(true, file);
    }

    public display(isMusicPlayer: boolean, file: FileDescriptor) {
        this.isMusicPlayer = isMusicPlayer;
        this.fileToPlay = file;

        if (this.isMusicPlayer) {
            this.popupTitle = 'Music Player';
        } else {
            this.popupTitle = 'Video Player';
        }

        this.popup.display();

    }

    public onClosePlayer() {

        const currentPlayer = this.isMusicPlayer ? this.musicPlayer : this.videoPlayer;
        currentPlayer.stop();

    }


}

