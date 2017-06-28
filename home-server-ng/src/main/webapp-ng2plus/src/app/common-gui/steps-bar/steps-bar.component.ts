import { StepsBuilder } from './steps-builder.service';
import { Step } from './step.modele';
import { Component, OnInit, Input } from '@angular/core';

@Component({
    selector: 'homeserver-steps-bar',
    templateUrl: 'steps-bar.component.html',
    styleUrls: ['steps-bar.component.scss']
})
export class StepsBarComponent implements OnInit {

    @Input()
    public steps: Array<Step>;

    constructor() { }

    ngOnInit() { }

    private findCurrentStepIndex(): number {
        return this.steps.filter(step => step.isCurrent)[0].index;
    }

    public nextStep(): void {
        const currentIndex = this.findCurrentStepIndex();
        if (currentIndex < this.steps.length - 1) {
            this.setCurrentStep(currentIndex + 1);
        }
    }

    public previousStep(): void {
        const currentIndex = this.findCurrentStepIndex();
        if (currentIndex - 1 >= 0) {
            this.setCurrentStep(currentIndex - 1);
        }
    }

    private setCurrentStep(newCurrentStepIndex: number): void {
        this.steps[newCurrentStepIndex].current();
        StepsBuilder.setCurrentStep(newCurrentStepIndex, this.steps);
    }

    private firstStep(): void {
        this.setCurrentStep(0);
    }
}

