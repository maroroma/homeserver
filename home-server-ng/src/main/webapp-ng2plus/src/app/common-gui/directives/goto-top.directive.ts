import { Directive, ElementRef, Input, HostListener, OnChanges, Renderer2 } from '@angular/core';

@Directive({ selector: '[homeserverGoToTop]' })
export class GoToTopDirective {

    @HostListener('click')
    public onClick(): void {
        window.scrollTo(0, 0);
    }

    constructor(private el: ElementRef, private renderer: Renderer2) {
        //${el.nativeElement.style.backgroundColor = 'yellow';}
        const decoratedElement = this.el.nativeElement as HTMLButtonElement;

        decoratedElement.className = 'btn btn-primary';

        // construction toottip en deux éléments
        const tooltipContainer = this.renderer.createElement('span') as HTMLSpanElement;
        this.renderer.setAttribute(tooltipContainer, 'class', 'glyphicon glyphicon-arrow-up');
        decoratedElement.appendChild(tooltipContainer);
        this.renderer.appendChild(decoratedElement, tooltipContainer);

    }
}