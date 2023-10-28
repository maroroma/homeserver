import React, {useEffect, useState} from 'react';
import {when} from '../../../tools/when';

export default function FloatingMenuComponent({ isOpen = false, children, hide = false }) {
    const [actionMenuInstances, setActionMenuInstances] = useState(undefined);

    useEffect(() => {
        var elems = document.querySelectorAll('.fixed-action-btn');
        var instances = window.M.FloatingActionButton.init(elems, {
            direction: 'left',
            hoverEnabled: false
        });

        setActionMenuInstances(instances);

        return () => {
            instances.forEach(oneInstance => oneInstance.destroy());
        }
    }, [hide]);

    useEffect(() => {
        if (actionMenuInstances) {
            if (isOpen) {
                actionMenuInstances.forEach(oneActionMenu => oneActionMenu.open());
            } else {
                actionMenuInstances.forEach(oneActionMenu => oneActionMenu.close());
            }
        }
    }, [actionMenuInstances, isOpen]);


    return (<div className={when(hide).thenHideElement("fixed-action-btn")}>
        <a href="#!" className="btn-floating btn-large red">
            <i className="large material-icons">menu</i>
        </a>
        <ul>
            {children}
        </ul>
    </div>);
}