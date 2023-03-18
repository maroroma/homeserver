import React, {useEffect} from 'react';
import {StepperComponent} from '../../commons/Stepper';
import {addBookSubReactor} from '../add/AddBookSubReactor';
import {useImportBookContext} from './ImportBookContext';

export default function ImportStepperComponent() {
    const { stepperDriver, dispatchSerieUpdated, dispatchExternalUpdateForStepDriver, dispatchExecuteImport } = useImportBookContext();

    useEffect(() => {
        const unsubscribeSerieSelected = addBookSubReactor().onSerieSelected(selectedSerie => {
            dispatchSerieUpdated(selectedSerie);
        });

        const unsubscribeUpdateDriver = stepperDriver.onDriverUpdated((newDriver) => {
            dispatchExternalUpdateForStepDriver(newDriver);
        });

        const unsubscriveComplete = stepperDriver.onComplete(() => {
            dispatchExecuteImport();
        })

        return () => {
            unsubscribeSerieSelected();
            unsubscribeUpdateDriver();
            unsubscriveComplete();
        }
    }, []);


    return <StepperComponent driver={stepperDriver} stepperId="importBooksStepper">
    </StepperComponent>
}