import { TooltipDirective } from './directives/tooltip.directive';
import { PopupPlayerComponent } from './players/popup-player/popup-player.component';
import { PlayedItemComponent } from './players/played-item/played-item.component';
import { VideoPlayerComponent } from './players/video-player/video-player.component';
import { FileBrowserToolBarComponent } from './file-browser/file-browser-toolbar/file-browser-toolbar.component';
import { FileBrowserRenameComponent } from './file-browser/file-browser-rename/file-browser-rename.component';
import { FileBrowserDownloadComponent } from './file-browser/file-browser-download/file-browser-download.component';
import { ByteFormatPipe } from './pipes/byte-format.pipe';
import { AutoFocusDirective } from './directives/auto-focus.directive';
import { ImageViewerComponent } from './image-viewer/image-viewer.component';
import { EditCollapsablePanelComponent } from './edit-collapsable-panel/edit-collapsable-panel.component';
import { CancelableInputTextComponent } from './cancelable-input-text/cancelable-input-text.component';
import { PageHeaderSearchService } from './page-header/page-header-search.service';
import { FileBrowserService } from './file-browser/file-browser.service';
import { FileBrowserComponent } from './file-browser/file-browser.component';
import { ToggleButtonDirective } from './directives/toggle-button.directive';
import { FileGlyphiconResolverPipe } from './pipes/file-glyphicon-resolver.pipe';
import { StepsBarComponent } from './steps-bar/steps-bar.component';
import { ImportFileButtonComponent } from './import-file-button/import-file-button.component';
import { AnchorExportDirective } from './directives/anchor-export.directive';
import { PopupComponent } from './popup/popup.component';
import { NotifyerService } from './notifyer/notifyer.service';
import { NotifyerComponent } from './notifyer/notifyer.component';
import { SDInputValueDirective } from './simple-datagrid/sd-input-value.directive';
import { SimpleDatagridComponent } from './simple-datagrid/simple-datagrid.component';
import { FormsModule } from '@angular/forms';
import { PrettyCheckboxComponent } from './pretty-checkbox/pretty-checkbox.component';
import { NavigationHelperService } from './navigation-helper.service';
import { PageHeaderComponent } from './page-header/page-header.component';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MenuComponent } from './menu/menu.component';
import { NgModule } from '@angular/core';
import { DownloadFileListComponent } from './file-browser/file-browser-download/download-file-list.component';
import { MusicPlayerComponent } from './players/music-player/music-player.component';
import { GoToTopDirective } from './directives/goto-top.directive';


@NgModule({
    imports: [CommonModule, RouterModule, FormsModule],
    exports: [MenuComponent, PageHeaderComponent, PrettyCheckboxComponent,
        SimpleDatagridComponent, SDInputValueDirective, NotifyerComponent,
        PopupComponent, AnchorExportDirective, GoToTopDirective, ToggleButtonDirective, TooltipDirective,
        ImportFileButtonComponent, StepsBarComponent, FileGlyphiconResolverPipe, FileBrowserComponent,
        CancelableInputTextComponent, EditCollapsablePanelComponent, ImageViewerComponent, AutoFocusDirective,
        ByteFormatPipe, FileBrowserDownloadComponent, DownloadFileListComponent, MusicPlayerComponent, VideoPlayerComponent, PlayedItemComponent, PopupPlayerComponent],
    declarations: [MenuComponent, PageHeaderComponent, PrettyCheckboxComponent,
        SimpleDatagridComponent, SDInputValueDirective, NotifyerComponent,
        PopupComponent, AnchorExportDirective, GoToTopDirective, ToggleButtonDirective, TooltipDirective,
        ImportFileButtonComponent, StepsBarComponent, FileGlyphiconResolverPipe, FileBrowserComponent,
        CancelableInputTextComponent, EditCollapsablePanelComponent, ImageViewerComponent, AutoFocusDirective,
        FileBrowserToolBarComponent, FileBrowserRenameComponent, ByteFormatPipe, FileBrowserDownloadComponent,
        DownloadFileListComponent, MusicPlayerComponent, VideoPlayerComponent, PlayedItemComponent, PopupPlayerComponent],
    providers: [NavigationHelperService, NotifyerService, FileBrowserService, PageHeaderSearchService],
})
export class CommonGUIModule { }
