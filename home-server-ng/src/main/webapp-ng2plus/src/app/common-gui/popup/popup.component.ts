import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import {
    trigger,
    state,
    style,
    animate,
    transition, keyframes
} from '@angular/animations';
import { HostListener } from '@angular/core';


@Component({
    selector: 'homeserver-popup',
    templateUrl: 'popup.component.html',
    styleUrls: ['popup.component.scss'],
    animations: [trigger('flyInOut', [
        state('display', style({ opacity: 1, display: 'block' })),
        state('hide', style({ opacity: 0, display: 'none' })),
        transition('hide => display', animate('300ms ease-in')),
        transition('display => hide', animate('100ms ease-out'))
    ])]
})
export class PopupComponent implements OnInit {

    @Input()
    public title: string;
    @Input()
    public footer = true;
    @Input()
    public closeButton = true;
    @Input()
    public okButton = true;
    @Input()
    public koButton = true;
    @Input()
    public okButtonLabel = 'ok';
    @Input()
    public koButtonLabel = 'annuler';


    @Output()
    public ok = new EventEmitter<any>();
    @Output()
    public ko = new EventEmitter<any>();

    /**
     * pilotage de l'animation d'affichage
     * @memberOf PopupComponent
     */
    public currentState = 'hide';

    public isVisible = false;


    constructor() { }

    public display() {
        this.isVisible = true;
        this.currentState = 'display';
    }

    public hide() {
        this.isVisible = false;
        this.currentState = 'hide';
    }

    /**
     * Gestion du clavier pour la fermeture de la popup.
     * @param event - 
     */
    @HostListener('window:keydown', ['$event'])
    public handleKeyBoard(event?: KeyboardEvent) {
        if (event.key === 'Escape') {
            this.hide();
        }
    }



    public innerOk() {
        this.hide();
        this.ok.emit();
    }

    public close() {
        this.hide();
        this.ko.emit();
    }

    ngOnInit() { }
}
