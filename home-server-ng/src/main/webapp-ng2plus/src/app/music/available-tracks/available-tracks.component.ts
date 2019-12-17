import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { DirectoryDescriptor } from '../../shared/directory-descriptor.modele';
import { FileBrowserOptionsBuilder } from '../../common-gui/file-browser/file-browser-options-builder.modele';
import { MusicService } from '../music.service';
import { FileDescriptor } from '../../shared/file-descriptor.modele';
import { NotifyerService } from '../../common-gui/notifyer/notifyer.service';


@Component({
    selector: 'homeserver-available-tracks',
    templateUrl: 'available-tracks.component.html',
    styleUrls: ['../../common-gui/styles/edit-zone.scss']
})
export class AvailableTracksComponent implements OnInit {

    public starterMockDirectory: DirectoryDescriptor;

    public selecteFilesToCopy: Array<FileDescriptor>;

    @Output()
    public goToNextStep = new EventEmitter<any>();

    public fileBrowserOptions = FileBrowserOptionsBuilder.prepare()
        .selection()
        .build();


    constructor(private musicService: MusicService, private notifyer: NotifyerService) {

    }

    ngOnInit() {
        this.musicService.getAvailableTracks().subscribe(res => {
            this.starterMockDirectory = new DirectoryDescriptor();
            this.starterMockDirectory.name = 'Disponibles';
            this.starterMockDirectory.files = res;
        });
    }

    public onSelectedFilesChange(selectedFiles: Array<FileDescriptor>): void {
        this.selecteFilesToCopy = selectedFiles;
    }

    public copyTracks(): void {
        this.musicService.copyAvailableTracksSelection(this.selecteFilesToCopy)
            .subscribe(res => {
                this.notifyer.showSuccess('Tous les fichiers ont été copiés dans le répertoire de travail');
                this.goToNextStep.emit();
            });
    }
}