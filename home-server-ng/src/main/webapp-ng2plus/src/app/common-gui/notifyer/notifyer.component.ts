import { NotifyerService } from './notifyer.service';
import { Component, OnInit } from '@angular/core';
import {
    trigger,
    state,
    style,
    animate,
    transition, keyframes
} from '@angular/animations';

/**
 * Composant graphique permettant d'afficher les notifications
 * grace au databinding sur le service de NotifyerService
 * @export
 * @class NotifyerComponent
 * @implements {OnInit}
 */
@Component({
    selector: 'homeserver-notifyer',
    templateUrl: 'notifyer.component.html',
    styleUrls: ['notifyer.component.scss'],
    animations: [trigger('flyInOut', [
        state('display', style({ opacity: 1 })),
        state('hide', style({ opacity: 0 })),
        transition('hide => display', animate('300ms ease-in')),
        transition('display => hide', animate('100ms ease-out'))
    ])]
})
export class NotifyerComponent implements OnInit {
    constructor(public notifyer: NotifyerService) { }

    ngOnInit() {

    }
}
