import { FileBrowserRenameComponent } from './file-browser/file-browser-rename.component';
import { FileBrowserToolBarComponent } from './file-browser/file-browser-toolbar.component';
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
import { HomeComponent } from './home/home.component';
import { CommonModule } from '@angular/common';
import { MenuComponent } from './menu/menu.component';
import { NgModule } from '@angular/core';


@NgModule({
    imports: [CommonModule, RouterModule, FormsModule],
    exports: [MenuComponent, HomeComponent, PageHeaderComponent, PrettyCheckboxComponent,
        SimpleDatagridComponent, SDInputValueDirective, NotifyerComponent,
        PopupComponent, AnchorExportDirective, ToggleButtonDirective,
        ImportFileButtonComponent, StepsBarComponent, FileGlyphiconResolverPipe, FileBrowserComponent,
        CancelableInputTextComponent, EditCollapsablePanelComponent, ImageViewerComponent, AutoFocusDirective],
    declarations: [MenuComponent, HomeComponent, PageHeaderComponent, PrettyCheckboxComponent,
        SimpleDatagridComponent, SDInputValueDirective, NotifyerComponent,
        PopupComponent, AnchorExportDirective, ToggleButtonDirective,
        ImportFileButtonComponent, StepsBarComponent, FileGlyphiconResolverPipe, FileBrowserComponent,
        CancelableInputTextComponent, EditCollapsablePanelComponent, ImageViewerComponent, AutoFocusDirective,
        FileBrowserToolBarComponent, FileBrowserRenameComponent],
    providers: [NavigationHelperService, NotifyerService, FileBrowserService, PageHeaderSearchService],
})
export class CommonGUIModule { }
