import { FileBrowserModule } from './filemanager/filemanager';
import { ReducerModule } from './reducer/reducer';
import { SeedboxModule } from './seedbox/seedbox';
import { routing } from './app.routing';
import { CommonGUIModule } from './common-gui/common-gui';
import { AdministrationModule } from './administration/administration';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AdministrationModule,
    CommonGUIModule,
    SeedboxModule,
    ReducerModule,
    FileBrowserModule,
    routing,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
