import { VisualItem } from './../../shared/visual-item.modele';
import { Directive, Input, ElementRef, HostListener, OnChanges, Output, EventEmitter } from '@angular/core';

@Directive(
    {
        selector: '[homeserverToggleButton]'
    }
)
export class ToggleButtonDirective implements OnChanges {
    private static readonly BTN_CSS = 'btn';
    private static readonly BTN_CSS_DEFAULT_SIZE = 'btn-sm';
    private static readonly BTN_CSS_UNSELECTED_COLOR = 'secondary-color';
    private static readonly BTN_CSS_SELECTED_COLOR = 'secondary-color-dark';

    @Input()
    homeserverToggleButton: VisualItem<any>;

    @Output()
    toggle = new EventEmitter<VisualItem<any>>();

    @Input()
    homeserverToggleButtonSize = ToggleButtonDirective.BTN_CSS_DEFAULT_SIZE;

    @HostListener('click')
    public onClick(): void {
        this.homeserverToggleButton.toggleSelection();
        this.generateStyle();
        this.toggle.emit(this.homeserverToggleButton);
    }

    private generateStyle(): void {
        let cssClazz = ToggleButtonDirective.BTN_CSS + ' ' + this.homeserverToggleButtonSize + ' ';
        if (this.homeserverToggleButton.selected) {
            cssClazz = cssClazz + ToggleButtonDirective.BTN_CSS_SELECTED_COLOR;
        } else {
            cssClazz = cssClazz + ToggleButtonDirective.BTN_CSS_UNSELECTED_COLOR;
        }

        const domElement = this.el.nativeElement as HTMLButtonElement;
        domElement.className = cssClazz;
    }


    constructor(private el: ElementRef) { }

    public ngOnChanges() {
        this.generateStyle();
    }

}

