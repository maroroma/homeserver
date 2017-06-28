import { CommitMoveRequestComponent } from './sort-downloaded-files/commit-move-request/commit-move-request.component';
import { TargetDirectoryListComponent } from './sort-downloaded-files/target-directory-list/target-directory-list.component';
import { CompletedFileListComponent } from './sort-downloaded-files/completed-file-list/completed-file-list.component';
;
import { SortDownloadedFilesService } from './sort-downloaded-files/sort-downloaded-files.service';
import { SortDownloadedFilesComponent } from './sort-downloaded-files/sort-downloaded-files.component';
import { CommonGUIModule } from './../common-gui/common-gui';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SeedboxComponent } from './seedbox.component';
import { NgModule } from '@angular/core';


@NgModule({
    imports: [CommonGUIModule, FormsModule, CommonModule],
    exports: [SeedboxComponent],
    declarations: [SeedboxComponent, SortDownloadedFilesComponent, CompletedFileListComponent,
        TargetDirectoryListComponent, CommitMoveRequestComponent],
    providers: [SortDownloadedFilesService],
})
export class SeedboxModule { }
