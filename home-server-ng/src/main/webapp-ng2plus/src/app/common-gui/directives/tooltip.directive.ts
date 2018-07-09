import { Directive, OnChanges, ElementRef, Input, Renderer2 } from '@angular/core';

@Directive({ selector: '[homeserverTooltip]' })
export class TooltipDirective implements OnChanges {


    @Input()
    homeserverTooltip: string;

    constructor(private el: ElementRef, private renderer: Renderer2) { }


    public ngOnChanges() {

        // élément enrichit
        const decoratedElement = this.el.nativeElement as HTMLElement;
        // container parent
        const parentElement = decoratedElement.parentElement;


        // construction toottip en deux éléments
        const tooltipContainer = this.renderer.createElement('div') as HTMLDivElement;
        const tooltipContent = this.renderer.createElement('div') as HTMLDivElement;
        this.renderer.appendChild(tooltipContainer, tooltipContent);
        tooltipContent.innerText = this.homeserverTooltip;
        this.renderer.setAttribute(tooltipContainer, 'class', 'tooltip-container');
        this.renderer.setAttribute(tooltipContent, 'class', 'tooltip-content');

        // apparition du tooltip
        decoratedElement.addEventListener('mouseover', (event) => {
            this.renderer.insertBefore(decoratedElement.parentElement, tooltipContainer, decoratedElement);
        });

        // disparition
        decoratedElement.addEventListener('mouseout', (event) => {
            this.renderer.removeChild(parentElement, tooltipContainer);
        });
    }
}
