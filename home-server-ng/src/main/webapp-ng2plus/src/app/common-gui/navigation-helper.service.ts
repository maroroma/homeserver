import { ModuleHandler } from './../administration/plugins/module-handler.modele';
import { AdministrationService } from './../administration/administration.service';
import { Injectable } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class NavigationHelperService {

    public currentModule: ModuleHandler;
    public moduleChanged: Subject<ModuleHandler>;

    constructor(private router: Router, private activatedRoute: ActivatedRoute, private adminService: AdministrationService) {
        this.moduleChanged = new Subject<ModuleHandler>();
    }

    public init() {
        // console.log(this.router.routerState.snapshot.url);
        this.getCurrentModuleAndNotify(this.router.routerState.snapshot.url);
        // this.currentModule = this.adminService.findModuleById(this.router.routerState.snapshot.url);
        // this.moduleChanged.next(this.currentModule);

        this.router.events.subscribe(next => {
            // console.log(next);
            if (next instanceof NavigationEnd) {
                // this.currentModule = this.adminService.findModuleById(next.url.replace('/', ''));
                // console.log('Module en cours', this.currentModule);
                // this.moduleChanged.next(this.currentModule);
                this.getCurrentModuleAndNotify(next.url);
            }
        });
    }

    private getCurrentModuleAndNotify(url: string) {
        this.currentModule = this.adminService.findModuleById(url.replace('/', ''));
        this.moduleChanged.next(this.currentModule);
    }

}

