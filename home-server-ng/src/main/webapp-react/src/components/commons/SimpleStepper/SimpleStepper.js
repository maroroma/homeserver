import React, {useEffect, useState} from 'react';
import {DisplayList} from '../../../tools/displayList';
import './SimpleStepper.scss';
import enhance from '../../../tools/enhance';
import {ActionMenuComponent} from '../ActionMenuComponent';
import {when} from '../../../tools/when';
import keys from '../../../tools/keys';

/**
 * Pour l'instant je pars là dessus, j'ai des galères avec les this.selectedIndex qui me retournent des valeurs
 * chelou
 * @returns 
 */
export function stepperConfiguration() {

    const disableAllSteps = (previousConfiguration) => {
        return {
            ...previousConfiguration,
            stepsConfiguration: previousConfiguration.stepsConfiguration.updateItems(oneItem => oneItem.disable())
        }
    }

    const enableCompleteAction = (previousConfiguration) => {
        return {
            ...previousConfiguration,
            isCompleteActionEnabled: true
        }
    }

    const disableCompleteAction = (previousConfiguration) => {
        return {
            ...previousConfiguration,
            isCompleteActionEnabled: false
        }
    }

    const selectStep = (previousConfiguration, newSelectedIndex) => {

        const stepsConfigurationWithNewlySelectedStep = previousConfiguration.stepsConfiguration
            .updateAllSelectableItems(false)
            .updateSelectableItems(newSelectedIndex, true, oneItem => oneItem.index)
            .updateItems(oneItem => oneItem.index === newSelectedIndex ? oneItem.enable() : oneItem);



        const test = {
            ...previousConfiguration,
            selectedIndex: newSelectedIndex,
            stepsConfiguration: stepsConfigurationWithNewlySelectedStep,
            isPreviousStepEnabled: newSelectedIndex > 0,//&& !stepsConfigurationWithNewlySelectedStep.displayList[newSelectedIndex].disabled,
            isNextStepEnabled: newSelectedIndex < stepsConfigurationWithNewlySelectedStep.displayList.length - 1 && !stepsConfigurationWithNewlySelectedStep.displayList[newSelectedIndex + 1].disabled,
            isLastStep: newSelectedIndex === stepsConfigurationWithNewlySelectedStep.displayList.length - 1
        }
        return test;
    }

    const switchEnableState = (previousConfiguration, stepIndexToModify, switchFunction) => {
        const newStepsConfiguration = previousConfiguration.stepsConfiguration
            .updateItems(oneItem => oneItem.index === stepIndexToModify ? switchFunction(oneItem) : oneItem);

        return {
            ...previousConfiguration,
            stepsConfiguration: newStepsConfiguration,
            isNextStepEnabled: previousConfiguration.selectedIndex < newStepsConfiguration.displayList.length - 1 && !newStepsConfiguration.displayList[previousConfiguration.selectedIndex + 1].disabled
        }

    }

    const enableStep = (previousConfiguration, stepIndexToEnable) => {
        return switchEnableState(previousConfiguration, stepIndexToEnable, (oneItem) => oneItem.enable());
    }

    const disableStep = (previousConfiguration, stepIndexToDisable) => {
        return switchEnableState(previousConfiguration, stepIndexToDisable, (oneItem) => oneItem.disable());
    }

    const initialize = (stepsDescription) => {
        let initialConfiguration = {
            stepsConfiguration: new DisplayList(stepsDescription)
                .updateItems(enhance().indexed())
                .updateItems(enhance().selectable())
                .updateItems(enhance().disablable()),
            selectedIndex: 0,
            isPreviousStepEnabled: false,
            isNextStepEnabled: false,
            isLastStep: false,
            isCompleteActionEnabled: false
        };

        initialConfiguration = disableAllSteps(initialConfiguration);
        initialConfiguration = selectStep(initialConfiguration, 0);
        return initialConfiguration;
    }



    return {
        initialize: initialize,
        selectStep: selectStep,
        disableAllSteps: disableAllSteps,
        enableStep: enableStep,
        disableStep: disableStep,
        enableCompleteAction: enableCompleteAction,
        disableCompleteAction: disableCompleteAction,
    }
}

export function SimpleStepper({
    simpleStepperConfiguration,
    onStepSelected = () => { },
    onNextStep = () => { },
    onPreviousStep = () => { },
    onComplete = () => { } }) {

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

        if (collapsibleInstance !== undefined) {
            collapsibleInstance.open(simpleStepperConfiguration.selectedIndex);
        }


        const keyEventHandler = (event) => {
            keys(event).onCtrlEnter(() => {
                event.preventDefault();
                if (simpleStepperConfiguration.isCompleteActionEnabled && simpleStepperConfiguration.isLastStep) {
                    onComplete();
                }

                if (simpleStepperConfiguration.isNextStepEnabled) {
                    onNextStep();
                }
            });
        }


        document.addEventListener("keydown", keyEventHandler);


        return () => {
            document.removeEventListener('keydown', keyEventHandler);
        }




    }, [simpleStepperConfiguration, collapsibleInstance]);

   

    const nextButton = simpleStepperConfiguration.isLastStep ?
        <a href="#!" title="Ctrl+Enter" className={when(!simpleStepperConfiguration.isCompleteActionEnabled).thenDisableElement("btn-floating btn-small green")} onClick={() => onComplete()}>
            <i className="material-icons">check</i>
        </a>
        :
        <a href="#!" title="Ctrl+Enter" className={when(!simpleStepperConfiguration.isNextStepEnabled).thenDisableElement("btn-floating btn-small blue")} onClick={() => onNextStep()}>
            <i className="material-icons">navigate_next</i>
        </a>


    return <>
        <ul className="collapsible stepper">
            {simpleStepperConfiguration
                .stepsConfiguration
                .displayList
                .map(oneStep => <SimpleStepComponent
                    stepConfiguration={oneStep}
                    key={`stepComponent-${oneStep.index}`}
                    onSelect={() => onStepSelected(oneStep)}>

                </SimpleStepComponent>)}
        </ul>

        <ActionMenuComponent alwaysOpen="true">
            <li>
                <a href="#!" className={when(!simpleStepperConfiguration.isPreviousStepEnabled).thenDisableElement("btn-floating btn-small blue")} onClick={() => onPreviousStep()}>
                    <i className="material-icons">navigate_before</i>
                </a>
            </li>
            <li>
                {nextButton}
            </li>
        </ActionMenuComponent>
    </>
}


export function SimpleStepComponent({ stepConfiguration, onSelect }) {

    let headerCss = "collapsible-header";
    headerCss = stepConfiguration.disabled ? `${headerCss}-disabled` : headerCss;

    const innerOnClick = () => {
        if (!stepConfiguration.disabled) {
            onSelect(stepConfiguration);
        }
    }



    return (
        <>
            <li >
                <div className={headerCss} onClick={innerOnClick}><i className="material-icons">{stepConfiguration.icon}</i>{stepConfiguration.title}</div>
                <div className="collapsible-body">{stepConfiguration.component}</div>
            </li>
        </>
    );


}