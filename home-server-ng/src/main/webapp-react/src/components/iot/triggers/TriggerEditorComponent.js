import React from 'react';
import { useEffect, useState } from 'react';
import { useDisplayList, DisplayList } from '../../../tools/displayList';

import "../../commons/Common.scss"

export default function TriggerEditorComponent({ trigger, triggerables }) {

    const [allTriggerables, setAllTriggerables] = useDisplayList();
    const [allTriggered, setAllTriggered] = useDisplayList();

    useEffect(() => {
        setAllTriggered({
            ...allTriggered.update(trigger.componentDescriptor.properties === null ? [] :
                trigger.componentDescriptor.properties
                    .filter(oneIotProperty => "TRIGGERS" === oneIotProperty.propertyName)
                    .flatMap(oneIotProperty => oneIotProperty.split("=")))
        });
    }, [trigger]);

    useEffect(() => {
        setAllTriggerables({
            ...allTriggerables.update(
                triggerables.map(oneComponent => {
                    return {
                        id: oneComponent.id,
                        name: oneComponent.componentDescriptor.name
                    }
                }))
        })

    }, [triggerables]);

    return (<ul className="collection with-header">
        <li className="collection-header"><h4>{trigger.componentDescriptor.name}</h4></li>

        <li className="collection-item avatar">
            <i className="material-icons circle">folder</i>
            <span className="title">Title</span>
            <p><input type="text" placeholder="Delai (secondes)"></input></p>
            <span href="#!" className="secondary-content">
                <i className="material-icons blue-font clickable" onClick={() => console.log("save")}>save</i>
                <i className="material-icons red-font clickable" onClick={() => console.log("delete")}>delete</i>
            </span>
        </li>
        <li className="collection-item avatar">
            <i className="material-icons circle">folder</i>
            <span className="title">Title</span>
            <p><input type="text" placeholder="Delai (secondes)"></input></p>
            <span href="#!" className="secondary-content">
                <i className="material-icons blue-font clickable" onClick={() => console.log("save")}>save</i>
                <i className="material-icons red-font clickable" onClick={() => console.log("delete")}>delete</i>
            </span>
        </li>

        <li className="collection-item"><div>&nbsp;<a href="#!" className="secondary-content"><i className="material-icons green-font">add</i></a></div></li>
    </ul>)
}