import React from 'react';
import { useEffect, useState } from 'react';
import eventReactor from '../../eventReactor/EventReactor';

const OPEN_OR_CLOSE_ACTION_MENU = "OPEN_OR_CLOSE_ACTION_MENU";

export function actionMenu() {
    const open = () => {
        eventReactor().emit(OPEN_OR_CLOSE_ACTION_MENU, true);
    }

    const close = () => {
        eventReactor().emit(OPEN_OR_CLOSE_ACTION_MENU, false);
    }

    return {
        open: open,
        close: close
    }
}


export function ActionMenuComponent({ children, alreadyOpen = false, alwaysOpen = false }) {

    const [actionMenuInstances, setActionMenuInstances] = useState(undefined);

    useEffect(() => {
        var elems = document.querySelectorAll('.fixed-action-btn');
        var instances = window.M.FloatingActionButton.init(elems, {
            direction: 'left',
            hoverEnabled: false
        });

        if (alreadyOpen || alwaysOpen) {
            instances.forEach(oneActionMenu => oneActionMenu.open());
        }

        if (alwaysOpen) {
            instances.forEach(oneInstance => oneInstance.destroy());
        }

        setActionMenuInstances(instances);

        return () => {
            instances.forEach(oneInstance => oneInstance.destroy());
        }
    }, [alreadyOpen]);

    useEffect(() => {
        return eventReactor().subscribe(OPEN_OR_CLOSE_ACTION_MENU, data => {
            if (actionMenuInstances) {
                if (data && data == true) {
                    actionMenuInstances.forEach(oneActionMenu => oneActionMenu.open());
                } else {
                    actionMenuInstances.forEach(oneActionMenu => oneActionMenu.close());
                }
            }
        })
    }, [actionMenuInstances]);


    return (<div className="fixed-action-btn">
        <a href="#!" className="btn-floating btn-large red">
            <i className="large material-icons">menu</i>
        </a>
        <ul>
            {children}
        </ul>
    </div>);
}