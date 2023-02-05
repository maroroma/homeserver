import React from 'react';
import eventReactor from '../../eventReactor/EventReactor';
import { useEffect, useState } from 'react';
import './Stepper.scss';
import { when } from '../../tools/when';
import enhance from '../../tools/enhance';
import on from '../../tools/on';
import { DisplayList } from '../../tools/displayList';
import { ActionMenuComponent } from './ActionMenuComponent';
import { searchSubReactor } from '../mainmenu/SearchBarComponent';

export const StepperDriverEvents = {
    DRIVER_UPDATED: 'DRIVER_UPDATED',
    ALL_STEPS_COMPLETE: 'ALL_STEPS_COMPLETE'
}

export class StepperDriver {
    constructor() {
        this.steps = new DisplayList([]);
        this.defaultSelected = undefined;
        this.appendStep = this.appendStep.bind(this);
        this.removeStep = this.removeStep.bind(this);
        this.insertStepBefore = this.insertStepBefore.bind(this);
        this.withSelected = this.withSelected.bind(this);
        this.disableAfterSelected = this.disableAfterSelected.bind(this);
        this.getSelectedIndex = this.getSelectedIndex.bind(this);
        this.nextStepButtonEnabled = false;
        this.previousStepButtonEnabled = false;
        this.enableNextStepButton = this.enableNextStepButton.bind(this);
        this.disableNextStepButton = this.disableNextStepButton.bind(this);
        this.enablePreviousStepButton = this.enablePreviousStepButton.bind(this);
        this.disablePreviousStepButton = this.disablePreviousStepButton.bind(this);
        this.innerNextStep = this.innerNextStep.bind(this);
        this.innerPreviousStep = this.innerPreviousStep.bind(this);
        this.selectStep = this.selectStep.bind(this);
        this.disableAfter = this.disableAfter.bind(this);
        this.nextStep = this.nextStep.bind(this);

        this.onDriverUpdated = this.onDriverUpdated.bind(this);

        this.isLastStep = this.isLastStep.bind(this);

        this.complete = this.complete.bind(this);
        this.onComplete = this.onComplete.bind(this);

    }

    appendStep(icon, title, component) {
        this.steps.update(this.steps.rawList.concat([{
            icon: icon,
            title: title,
            component: component
        }]))
            .updateItems(enhance().indexed())
            .updateItems(enhance().selectable())
            .updateItems(enhance().disablable());
        return this;
    }

    removeStep(stepIndexToRemove) {
        console.log("removeStep");
        this.steps.update(this.steps.rawList
            .filter((oneStep) => stepIndexToRemove !== oneStep.index)
            .map((oneStepToReindex, index) => { return { ...oneStepToReindex, index }; })
        );
        return this;
    }

    insertStepBefore(indexForInsertion, icon, title, component) {
        const stepsBefore = this.steps.rawList.filter(oneStep => oneStep.index < indexForInsertion);
        const stepsAfter = this.steps
            .rawList
            .filter(oneStep => oneStep.index >= indexForInsertion)
            .map(oneStepWithIndexToUpdate => {
                return { ...oneStepWithIndexToUpdate, index: (oneStepWithIndexToUpdate.index + 1), }
            });
        console.log(stepsAfter);
        let stepToInsert = enhance().indexed()({
            icon: icon,
            title: title,
            component: component
        });
        stepToInsert = enhance().selectable()(stepToInsert);
        stepToInsert = enhance().disablable()(stepToInsert);
        stepToInsert.index = indexForInsertion;
        this.steps.update(stepsBefore.concat([stepToInsert]).concat(stepsAfter));
        console.log(this.steps.rawList);
        return this;
    }

    withSelected(index) {

        const previousIndex = this.getSelectedIndex();


        this.steps = this.steps
            .updateAllSelectableItems(false)
            .updateSelectableItems(index, true, oneItem => oneItem.index)
            .updateItems(oneItem => {
                if (oneItem.selected && oneItem.disabled) {
                    oneItem.disabled = false;
                }

                return oneItem;
            });

        if (this.getSelectedIndex() > 0) {
            this.enablePreviousStepButton();
        } else {
            this.disablePreviousStepButton();
        }

        if (this.getSelectedIndex() + 1 < this.steps.rawList.length - 1) {
            if (this.steps.rawList[this.getSelectedIndex() + 1].disabled === true) {
                this.disableNextStepButton();
            } else {
                this.enableNextStepButton();
            }
        }

        if (previousIndex !== -1 && index !== previousIndex) {
            searchSubReactor().clearSearchBar();
        }

        return this;
    }

    disableAfterSelected() {
        this.steps = this.steps.updateItems((oneItem, index) => {
            if (index > this.getSelectedIndex()) {
                return oneItem.disable();
            }
            return oneItem;
        });
        return this;
    }

    disableAfter(indexFrom) {
        this.steps = this.steps.updateItems((oneItem, index) => {
            if (index > indexFrom) {
                return oneItem.disable();
            }
            return oneItem;
        });
        return this;
    }

    getSelectedIndex() {
        const selectedItem = this.steps.rawList.find(on().selected());
        return selectedItem ? selectedItem.index : -1;
    }

    enableNextStepButton() {
        this.nextStepButtonEnabled = true;
        return this;
    }

    disableNextStepButton() {
        this.nextStepButtonEnabled = false;
        return this;
    }

    enablePreviousStepButton() {
        this.previousStepButtonEnabled = true;
        return this;
    }

    disablePreviousStepButton() {
        this.previousStepButtonEnabled = false;
        return this;
    }

    innerNextStep() {
        eventReactor().emit(StepperDriverEvents.DRIVER_UPDATED, this.withSelected(this.getSelectedIndex() + 1));
        return this;
    }

    nextStep() {
        this.withSelected(this.getSelectedIndex() + 1)
        return this;
    }

    innerPreviousStep() {
        eventReactor().emit(StepperDriverEvents.DRIVER_UPDATED, this.withSelected(this.getSelectedIndex() - 1));
        return this;
    }

    onDriverUpdated(driverUpdatedHandler) {
        return eventReactor().subscribe(StepperDriverEvents.DRIVER_UPDATED, driverUpdatedHandler);
    }

    selectStep(step) {
        if (this.getSelectedIndex() !== step.index) {
            eventReactor().emit(StepperDriverEvents.DRIVER_UPDATED, this.withSelected(step.index));
        }
        return this;
    }

    isLastStep() {
        return this.getSelectedIndex() === this.steps.rawList.length - 1;
    }

    complete() {
        eventReactor().emit(StepperDriverEvents.ALL_STEPS_COMPLETE);
        return this;
    }

    onComplete(onCompleteEventHandler) {
        return eventReactor().subscribe(StepperDriverEvents.ALL_STEPS_COMPLETE, onCompleteEventHandler);
    }
}

export function StepComponent({ step, onSelect }) {

    let headerCss = "collapsible-header";
    headerCss = step.disabled ? `${headerCss}-disabled` : headerCss;

    const innerOnClick = () => {
        if (!step.disabled) {
            onSelect(step);
        }
    }

    return (
        <li onClick={innerOnClick}>
            <div className={headerCss}><i className="material-icons">{step.icon}</i>{step.title}</div>
            <div className="collapsible-body">{step.component}</div>
        </li>
    );


}


export function StepperComponent({ driver, stepperId = "defaultStepperId" }) {

    const [collapsibleInstance, setCollapsibleInstance] = useState(undefined);


    useEffect(() => {
        var elems = document.querySelectorAll('.stepper');
        const collapsibleInstance = window.M.Collapsible.init(elems, {})[0];
        setCollapsibleInstance(collapsibleInstance);




        return () => {
            collapsibleInstance.destroy();
        };
    }, []);

    useEffect(() => {
        if (collapsibleInstance) {
            collapsibleInstance.open(driver.getSelectedIndex())
        }


        const keyEventHandler = (event) => {
            if (event.ctrlKey && event.keyCode === 13 && driver.nextStepButtonEnabled) {
                event.preventDefault();
                if (driver.isLastStep()) {
                    driver.complete();
                } else {
                    driver.innerNextStep();
                }
            }
        }

        document.addEventListener("keydown", keyEventHandler);

        return () => {
            document.removeEventListener('keydown', keyEventHandler);
        };


    }, [driver, collapsibleInstance]);

    const onStepSelected = (step) => {
        driver.selectStep(step);
    }

    const nextButton = driver.isLastStep() ? <a href="#!" title="Ctrl+Enter" className={when(!driver.nextStepButtonEnabled).thenDisableElement("btn-floating btn-small green")} onClick={() => driver.complete()}>
        <i className="material-icons">check</i>
    </a> : <a href="#!" title="Ctrl+Enter" className={when(!driver.nextStepButtonEnabled).thenDisableElement("btn-floating btn-small blue")} onClick={() => driver.innerNextStep()}>
        <i className="material-icons">navigate_next</i>
    </a>


    return (
        <>
            <ul className="collapsible stepper">
                {driver.steps.displayList.map(oneStep => <StepComponent step={oneStep} key={`stepComponent-${stepperId}-${oneStep.index}`} onSelect={onStepSelected}></StepComponent>)}
            </ul>
            <ActionMenuComponent alwaysOpen="true">
                <li>
                    <a href="#!" className={when(!driver.previousStepButtonEnabled).thenDisableElement("btn-floating btn-small blue")} onClick={() => driver.innerPreviousStep()}>
                        <i className="material-icons">navigate_before</i>
                    </a>
                </li>
                <li>
                    {nextButton}
                </li>
            </ActionMenuComponent>
        </>

    )
}