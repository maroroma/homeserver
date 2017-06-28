import { Subscription } from 'rxjs/Rx';
import { PageHeaderSearchService } from './page-header-search.service';
import { ModuleHandler } from './../../administration/plugins/module-handler.modele';
import { SubMenuItem } from './sub-menu-item.modele';
import { NavigationHelperService } from './../navigation-helper.service';
import { Component, OnInit, Input, EventEmitter, Output, OnDestroy } from '@angular/core';

@Component({
    selector: 'homeserver-page-header',
    templateUrl: 'page-header.component.html',
    styleUrls: ['page-header.component.scss']
})
export class PageHeaderComponent implements OnInit, OnDestroy {

    public module: ModuleHandler;

    /**
     * Liste des sous items à afficher
     * @type {Array<SubMenuItem>}
     * @memberOf PageHeaderComponent
     */
    @Input()
    public subMenuItems: Array<SubMenuItem>;

    @Input()
    public genericSearch = false;


    searchField: string;
    clearSearchSubscription: Subscription;

    @Output()
    public itemSelected = new EventEmitter<SubMenuItem>();

    public selectedItem: SubMenuItem;

    public isSubMenuDisplayed = false;


    constructor(private navHelper: NavigationHelperService, private searchService: PageHeaderSearchService) { }

    public displaySubMenu(): void {
        if (this.subMenuItems) {
            this.isSubMenuDisplayed = true;
        }
    }

    public hideSubMenu(): void {
        this.isSubMenuDisplayed = false;
    }

    selectItem(item: SubMenuItem): void {
        this.subMenuItems.forEach(si => si.selected = false);
        item.selected = true;
        this.selectedItem = item;
        this.itemSelected.emit(this.selectedItem);
        this.hideSubMenu();
        this.searchService.clearSearchField();
    }

    ngOnInit() {

        this.clearSearchSubscription = this.searchService.searchCleared.subscribe(res => this.searchField = res);

        this.module = this.navHelper.currentModule;
        this.navHelper.moduleChanged.subscribe(handler => {
            this.module = handler;
        });
        if (this.subMenuItems) {
            this.selectedItem = this.subMenuItems.find(subItem => subItem.selected);
        }

    }

    ngOnDestroy() {
        this.clearSearchSubscription.unsubscribe();
    }

    onKey() {
        this.searchService.updateSearchField(this.searchField);
    }
}
