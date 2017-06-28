import { Step } from './step.modele';
export class StepsBuilder {
    steps: Array<Step> = new Array<Step>();
    currentBuildingStep = new Step();


    public static setCurrentStep(newCurrentStepIndex: number, steps: Array<Step>): void {
        steps.filter(step => step.index < newCurrentStepIndex).forEach(completedStep => completedStep.completed());
        steps.filter(step => step.index > newCurrentStepIndex).forEach(completedStep => completedStep.todo());
        console.log(steps);
    }

    public addStep(label: string): StepsBuilder {
        this.currentBuildingStep = new Step();
        this.currentBuildingStep.label = label;
        this.steps.push(this.currentBuildingStep);
        return this;
    }

    public current(): StepsBuilder {
        this.currentBuildingStep.isCurrent = true;
        return this;
    }


    public build(): Array<Step> {

        let currentIndex = 0;

        this.steps.forEach((oneBuilder, index) => {
            oneBuilder.index = index;
            if (oneBuilder.isCurrent) {
                currentIndex = index;
            }
        });

        if (currentIndex === 0) {
            this.steps[0].isCurrent = true;
        }

        StepsBuilder.setCurrentStep(currentIndex, this.steps);


        return this.steps;

    }




}

