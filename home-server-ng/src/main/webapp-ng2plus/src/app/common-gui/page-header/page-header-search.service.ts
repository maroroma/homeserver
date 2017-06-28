import { Subject } from 'rxjs/Subject';
import { Injectable } from '@angular/core';

@Injectable()
export class PageHeaderSearchService {

    public searchChanged: Subject<string>;
    public searchCleared: Subject<string>;

    public searchData: string;


    constructor() {
        this.searchChanged = new Subject<string>();
        this.searchCleared = new Subject<string>();
    }


    public updateSearchField(searchData: string): void {
        this.searchData = searchData;
        this.searchChanged.next(this.searchData);
    }

    public clearSearchField(): void {
        this.searchData = '';
        this.searchCleared.next(this.searchData);
    }
}
