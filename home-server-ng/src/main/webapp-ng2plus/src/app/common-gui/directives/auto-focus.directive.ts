import { VisualItem } from './../../shared/visual-item.modele';
import { Directive, Input, ElementRef, HostListener, OnChanges, Output, EventEmitter } from '@angular/core';

@Directive(
    {
        selector: '[homeserverAutofocus]'
    }
)
export class AutoFocusDirective implements OnChanges {

    @Input()
    homeserverAutofocus = false;


    @HostListener('blur')
    public onBlur(): void {
        console.log('onblur');
        if (this.homeserverAutofocus) {
            (this.el.nativeElement as HTMLElement).focus();
        }
    }




    constructor(private el: ElementRef) {
        (this.el.nativeElement as HTMLElement).focus();
    }

    public ngOnChanges() {
        if (this.homeserverAutofocus) {
            (this.el.nativeElement as HTMLElement).focus();
        }
    }

}

