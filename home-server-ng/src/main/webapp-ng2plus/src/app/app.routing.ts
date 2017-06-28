import { FileManagerComponent } from './filemanager/filemanager.component';
import { ReducerComponent } from './reducer/reducer.component';
import { SeedboxComponent } from './seedbox/seedbox.component';
import { AdministrationComponent } from './administration/administration.component';
import { HomeComponent } from './common-gui/home/home.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  //   { path: 'administration', component: StarterComponent },
  { path: '', component: HomeComponent },
  { path: 'administration', component: AdministrationComponent },
  { path: 'seedbox', component: SeedboxComponent },
  { path: 'reducer', component: ReducerComponent },
  { path: 'filemanager', component: FileManagerComponent },
];

export const routing = RouterModule.forRoot(routes);
