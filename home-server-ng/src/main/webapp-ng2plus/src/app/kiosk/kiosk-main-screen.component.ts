import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'homeserver-kiosk-main-screen',
    templateUrl: 'kiosk-main-screen.component.html',
    styleUrls: ['kiosk-main-screen.component.scss']
})

export class KioskMainScreenComponent implements OnInit {

    public isKiosk = false;

    constructor() { }

    ngOnInit() { }

    public displayKiosk(): void {
        this.isKiosk = true;
        document.documentElement.requestFullscreen();
    }

    public hideKiosk(): void {
        this.isKiosk = false;
        document.exitFullscreen();
    }
}