import { NavigationHelperService } from './common-gui/navigation-helper.service';
import { AdministrationService } from './administration/administration.service';
import { Component, OnInit } from '@angular/core';
import { Http, Response } from '@angular/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'app works!';

  constructor(private http: Http,
    private adminService: AdministrationService,
    private navHelper: NavigationHelperService) {

  }

  public testProxy() {
    this.http.get('api/administration/version')
      .subscribe(response => {
        console.log(response.json());
      });
  }

  public ngOnInit() {
    this.adminService.loadAllModules().subscribe(res => {
      this.navHelper.init();
      console.log("modules chargés");
    });

  }
}
