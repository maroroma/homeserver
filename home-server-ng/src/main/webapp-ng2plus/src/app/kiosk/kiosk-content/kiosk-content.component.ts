import { Subscription } from 'rxjs/Rx';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { TimerObservable } from 'rxjs/observable/TimerObservable';
import { KioskService } from 'app/kiosk/kiosk.service';
import { KioskDisplayOption } from 'app/kiosk/model/kiosk-display-options.modele';

@Component({
    selector: 'homeserver-kiosk-content',
    templateUrl: 'kiosk-content.component.html',
    styleUrls: ['kiosk-content.component.scss']
})

export class KioskContentComponent implements OnInit, OnDestroy {

    public time: Date;
    timerSubscription: Subscription;
    public displayOptions: KioskDisplayOption;

    constructor(private kioskService: KioskService) { }

    ngOnInit() {
        this.time = new Date();

        this.kioskService.getDisplayOptions().subscribe(res => {
            this.displayOptions = res;
        })

        // les appels se font toutes les secondes, ça évite de se coltiner l'interface sse pour le TS.
        this.timerSubscription = TimerObservable.create(0, 1000).subscribe(tres => {
            this.time = new Date();
        });
    }

    ngOnDestroy() {
        this.timerSubscription.unsubscribe();
    }
}
