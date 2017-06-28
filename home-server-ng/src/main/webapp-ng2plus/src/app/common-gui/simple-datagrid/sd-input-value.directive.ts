import { element } from 'protractor';
import { SimpleDatagridFieldDescriptor } from './simple-datagrid-field-descriptor.modele';
import { VisualItem } from './../../shared/visual-item.modele';
import { Directive, ElementRef, HostListener, Input, OnChanges, EventEmitter, Output } from '@angular/core';


/**
 * Directive pour la substitution au ngModel qui n'est pas suffisant dans
 * le cadre de la datagrid.
 * @export
 * @class SDInputValueDirective
 * @implements {OnChanges}
 */
@Directive({
    selector: '[homeserverSDInputValue]'
})
export class SDInputValueDirective implements OnChanges {

    @Input()
    homeserverSDFieldDescriptor: SimpleDatagridFieldDescriptor;

    @Input()
    homeserverSDInputValue: VisualItem<any>;

    @Output()
    homeserverSDInputValueChanged = new EventEmitter<VisualItem<any>>();

    constructor(private el: ElementRef) { }

    @HostListener('keyup')
    onChange(): void {
        this.homeserverSDFieldDescriptor.writeData(this.homeserverSDInputValue, (this.el.nativeElement as HTMLInputElement).value);
        this.homeserverSDInputValueChanged.emit(this.homeserverSDInputValue);
    }

    @HostListener('focus')
    onFocus(): void {
        // console.log('focus');
        // this.el.nativeElement.style.backgroundColor = 'yellow';
    }

    @HostListener('blur')
    onBlur(): void {
        // console.log('blur');
    }

    public ngOnChanges() {
        const inputElement = this.el.nativeElement as HTMLInputElement;
        inputElement.value = this.homeserverSDFieldDescriptor.resolveData(this.homeserverSDInputValue);
    }
}
