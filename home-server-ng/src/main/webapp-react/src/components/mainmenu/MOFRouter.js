import React from 'react';

import { useRef, useEffect } from 'react';

import modulesAdapter from './ModulesAdapter';


export default function MOFRouter({ selectedPath }) {

    console.log("MOFRouter", selectedPath);


    const componentToDisplay = selectedPath ? (modulesAdapter().getMenuDescriptorForPath(selectedPath).component):null;

    return (
        <>
            {componentToDisplay}
        </>
    );
}