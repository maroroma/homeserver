import { Step } from "./step.modele";

import { StepsBuilder } from "./steps-builder.service";

export class Steps {
    public steps: Array<Step>;
    public nbSteps = 0;
    public progressPercent = 0;
    public currentStep: Step;

    private findCurrentStepIndex(): number {
        return this.steps.filter(step => step.isCurrent)[0].index;
    }

    public isCurrent(indexToTest: number): boolean {
        return this.findCurrentStepIndex() === indexToTest;
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

    public setCurrentStep(newCurrentStepIndex: number): void {
        this.currentStep = this.steps[newCurrentStepIndex];
        this.currentStep.current();
        this.steps.filter(step => step.index < newCurrentStepIndex).forEach(completedStep => completedStep.completed());
        this.steps.filter(step => step.index > newCurrentStepIndex).forEach(completedStep => completedStep.todo());
        this.updateStatus();
    }

    public firstStep(): void {
        this.setCurrentStep(0);
    }

    private updateStatus(): void {
        this.nbSteps = this.steps.length;
        this.progressPercent = (this.findCurrentStepIndex() + 1) / this.nbSteps * 100;
    }

}