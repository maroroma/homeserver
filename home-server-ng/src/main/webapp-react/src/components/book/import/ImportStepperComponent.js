import React, {useEffect} from 'react';
import {useImportBookContext} from './ImportBookContext';
import bookApi from '../../../apiManagement/BookApi';
import {SimpleStepper} from '../../commons/SimpleStepper/SimpleStepper';

export default function ImportStepperComponent() {
    const {
        stepperConfiguration,
        dispatchSerieUpdated,
        dispatchExecuteImport,
        dispatchExecuteLoadProposals,
        dispatchNextStepRequired,
        dispatchManualStepRequired,
        dispatchPreviousStepRequired
    } = useImportBookContext();

    useEffect(() => {
        // ici on gère l'auto sélection d'une série via les queryPArams
        const urlParams = new URLSearchParams(window.location.search);
        const serieIdFromQueryParam = urlParams.get("serieId");
        if (serieIdFromQueryParam !== undefined && serieIdFromQueryParam !== null) {
            console.log("on charge automatiquement la série", serieIdFromQueryParam);
            bookApi().getOneSerie(serieIdFromQueryParam)
                .then(result => {
                    console.log("autoload serie", result);
                    dispatchSerieUpdated(result);
                    if (result.serieUrlForImport !== undefined) {
                        dispatchExecuteLoadProposals();
                    }
                });
        }

    }, []);

    const onNextStep = () => dispatchNextStepRequired();

    const onComplete = () => dispatchExecuteImport();
    const onPreviousStep = () => dispatchPreviousStepRequired();
    const onStepSelected = (selectedStep) => dispatchManualStepRequired(selectedStep);

    return <>
        <SimpleStepper
            simpleStepperConfiguration={stepperConfiguration}
            onNextStep={onNextStep}
            onComplete={onComplete}
            onPreviousStep={onPreviousStep}
            onStepSelected={onStepSelected}
        ></SimpleStepper>
    </>
}