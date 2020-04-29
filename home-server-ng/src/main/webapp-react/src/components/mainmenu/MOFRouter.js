import React from 'react';

import modulesAdapter from './ModulesAdapter';

import './MOFRouter.scss';

export default function MOFRouter({ selectedPath }) {

    const menuDescritor = selectedPath ? modulesAdapter().getMenuDescriptorForPath(selectedPath) : null;
    const componentToDisplay = menuDescritor ? menuDescritor.component : null;
    const dontUseDefaultPanel = menuDescritor ? menuDescritor.dontUseDefaultPanel : false;

    return (
        <>
            <div className={dontUseDefaultPanel ? "" : "current-module"} >
                {componentToDisplay}
            </div>
        </>
    );
}