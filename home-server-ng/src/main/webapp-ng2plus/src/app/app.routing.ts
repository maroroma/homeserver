import { HomeComponent } from './home/home.component';
import { MusicComponent } from './music/music.component';
import { FileManagerComponent } from './filemanager/filemanager.component';
import { ReducerComponent } from './reducer/reducer.component';
import { SeedboxComponent } from './seedbox/seedbox.component';
import { AdministrationComponent } from './administration/administration.component';
import { RouterModule, Routes } from '@angular/router';
import { KioskMainScreenComponent } from 'app/kiosk/kiosk-main-screen.component';
import { DirectDownloadComponent } from './filemanager/direct-download/direct-download.component';
import { IotComponent } from './iot/iot-component';

const routes: Routes = [
  //   { path: 'administration', component: StarterComponent },
  { path: '', component: HomeComponent },
  { path: 'administration', component: AdministrationComponent },
  { path: 'seedbox', component: SeedboxComponent },
  { path: 'reducer', component: ReducerComponent },
  { path: 'filemanager', component: FileManagerComponent },
  { path: 'filemanager/directdownload', component: DirectDownloadComponent },
  { path: 'music', component: MusicComponent },
  { path: 'kiosk', component: KioskMainScreenComponent },
  { path: 'iot', component: IotComponent }
];

export const routing = RouterModule.forRoot(routes, { useHash: true });
