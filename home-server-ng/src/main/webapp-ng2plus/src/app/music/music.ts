import { DownloadTracksComponent } from './download-tracks/download-tracks.component';
import { TrackListComponent } from './track-list/track-list.component';
import { UploadTracksComponent } from './upload-tracks/upload-tracks.component';
import { AlbumRendererComponent } from './album-renderer/album-renderer.component';
import { AddAlbumArtComponent } from './add-albumart/add-albumart.component';
import { MusicService } from './music.service';
import { AlbumDescriptorComponent } from './album-descriptor/album-descriptor.component';
import { MusicComponent } from './music.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommonGUIModule } from './../common-gui/common-gui';
import { NgModule } from '@angular/core';
import { AlbumListComponent } from './album-list/album-list.component';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [MusicComponent],
    declarations: [MusicComponent, AlbumDescriptorComponent, AddAlbumArtComponent, AlbumRendererComponent,
        UploadTracksComponent, TrackListComponent, DownloadTracksComponent, AlbumListComponent],
    providers: [MusicService]
})
export class MusicModule { }
