import React from 'react';
import {ImportBookProvider} from './ImportBookContext';
import ImportStepperComponent from './ImportStepperComponent';

export default function ImportBooksWithProviderComponent() {

    return <ImportBookProvider>
        <ImportStepperComponent></ImportStepperComponent>
    </ImportBookProvider>
}