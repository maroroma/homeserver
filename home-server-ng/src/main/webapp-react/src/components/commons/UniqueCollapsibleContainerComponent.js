import React from 'react';
import { useEffect, useState } from 'react';
import eventReactor from '../../eventReactor/EventReactor';
import IconComponent from './IconComponent';

export function UniqueCollapsibleContainerComponent({ id, children, title = "default title" }) {

    const [expanded, setExpanded] = useState(false);
    const [collapsibleInstances, setCollapsibleInstances] = useState([]);

    useEffect(() => {


        const instances = window.M.Collapsible.init(document.querySelectorAll("#collapsibleContainer"), {});
        setCollapsibleInstances(instances);

        return () => {
            instances.forEach(oneInstance => oneInstance.destroy());
        }
    }, []);


    useEffect(() => {

        const unsubscribeCollapisbleEvent = uniqueCollapsibleContainerSubReactor().onCollapse(id, event => {
            collapsibleInstances.forEach(oneInstance => oneInstance.close());
        });

        return ()  => {
            unsubscribeCollapisbleEvent();
        }

    }, [id, collapsibleInstances]);


    const switchExpand = () => setExpanded(!expanded);

    return (
        <ul id="collapsibleContainer" className="collapsible">
            <li>
                <div className="collapsible-header">
                    <button className="btn btn-small waves-effect waves-light" onClick={switchExpand}>
                        {title}
                        <IconComponent icon={expanded ? "arrow_drop_up" : "arrow_drop_down"} classAddons="tiny right"></IconComponent>
                    </button>
                </div>
                <div className="collapsible-body">{children}</div>
            </li>
        </ul>
    );

};

export function uniqueCollapsibleContainerSubReactor() {

    const collapse = (collapsibleContainerId) => eventReactor().emit(`COLLAPSE_COLLAPSIBLE_${collapsibleContainerId}`);
    const onCollapse = (collapsibleContainerId, eventListener) => eventReactor().subscribe(`COLLAPSE_COLLAPSIBLE_${collapsibleContainerId}`, eventListener);


    return {
        collapse: collapse,
        onCollapse: onCollapse
    }
}