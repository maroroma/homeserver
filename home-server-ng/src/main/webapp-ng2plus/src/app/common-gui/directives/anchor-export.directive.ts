import { VisualItem } from './../../shared/visual-item.modele';
import { Directive, ElementRef, HostListener, Input, OnChanges, EventEmitter, Output } from '@angular/core';

@Directive(
    {
        selector: '[homeserverAnchorExport]'
    }
)
export class AnchorExportDirective implements OnChanges {
    private static readonly BTN_CSS = 'btn btn-primary btn-lg';

    @Input()
    homeserverAnchorExport: VisualItem<any>;


    constructor(private el: ElementRef) { }

    public ngOnChanges() {
        const aElement = this.el.nativeElement as HTMLAnchorElement;
        if (this.homeserverAnchorExport.exportable) {
            aElement.href = this.homeserverAnchorExport.exportUrl;
            aElement.className = AnchorExportDirective.BTN_CSS;
            aElement.download = this.homeserverAnchorExport.exportFileName;
        } else {
            aElement.href = null;
            aElement.className = AnchorExportDirective.BTN_CSS + ' disabled';

        }
    }

}
