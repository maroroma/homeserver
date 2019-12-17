import { StepsBuilder } from './steps-builder.service';
import { Step } from './step.modele';
import { Component, OnInit, Input } from '@angular/core';
import { Steps } from './steps.modele';

@Component({
    selector: 'homeserver-steps-bar',
    templateUrl: 'steps-bar.component-v2.html',
    styleUrls: ['steps-bar.component-v2.scss']
})
export class StepsBarComponent implements OnInit {

    @Input()
    public steps: Steps;


    constructor() { }

    ngOnInit() { }


    public nextStep(): void {
        this.steps.nextStep();
    }

    public previousStep(): void {
        this.steps.previousStep();
    }

    public firstStep(): void {
        this.steps.setCurrentStep(0);
    }
}

