import { FileDescriptor } from 'app/shared/file-descriptor.modele';
import { OnInit, Input, ViewChild, ElementRef, OnDestroy } from '@angular/core';



export abstract class AbstractPlayer implements OnInit, OnDestroy {


    @Input()
    public fileToPlay: FileDescriptor;

    @ViewChild('mediaPlayer') mediaPlayer: ElementRef;

    ngOnInit() { }

    ngOnDestroy() {
        this.stop();
    }

    public stop(): void {
        if (this.mediaPlayer && this.mediaPlayer.nativeElement) { this.mediaPlayer.nativeElement.pause(); }
    }


}