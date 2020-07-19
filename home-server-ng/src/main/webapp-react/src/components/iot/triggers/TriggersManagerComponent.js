import React from 'react';
import { useEffect, useState } from 'react';
import { useDisplayList } from '../../../tools/displayList';
import iotApi from '../../../apiManagement/IotApi';
import sort from '../../../tools/sort';

import "../../commons/Common.scss"
import TriggerEditorComponent from './TriggerEditorComponent';
import toaster from '../../commons/Toaster';
import { iotResolver, iotFilter } from '../IotRendererResolver';

export default function TriggersManagerComponent() {

    const [allTriggers, setAllTrigers] = useDisplayList();
    const [rawTriggerableComponents, setRawTriggerableComponents] = useState([]);

    useEffect(() => {
        iotApi().getTriggersComponent().then(response => setAllTrigers({
            ...allTriggers.update(response).updateSort(sort().basic(oneComponent => oneComponent.componentDescriptor.name))
        }));

        toaster().plopAndWait(() => iotApi().getAllIotComponents(), "Chargement des composants triggerables...")
            .then(response => response.filter(iotFilter().triggerables()))
            .then(response => setRawTriggerableComponents(response));
    }, []);

    // TODO comment afficher les triggers ?
    // un premier niveau racine, avec un résumé des trucs branchés
    // on clique sur un trigger, et là on a une page de détail où l'on peut jouer...


    return <div>
        {allTriggers.displayList.map((oneTrigger, index) => (<TriggerEditorComponent key={index} trigger={oneTrigger} triggerables={rawTriggerableComponents}></TriggerEditorComponent>))}
        <ul class="collection with-header">
            <li class="collection-header"><h4>Trigger Name</h4></li>

            <li class="collection-item avatar">
                <i class="material-icons circle">folder</i>
                <span class="title">Title</span>
                <p><input type="text" placeholder="Delai (secondes)"></input></p>
                <span href="#!" class="secondary-content">
                    <i class="material-icons blue-font clickable" onClick={() => console.log("save")}>save</i>
                    <i class="material-icons red-font clickable" onClick={() => console.log("delete")}>delete</i>
                </span>
            </li>
            <li class="collection-item avatar">
                <i class="material-icons circle">folder</i>
                <span class="title">Title</span>
                <p><input type="text" placeholder="Delai (secondes)"></input></p>
                <span href="#!" class="secondary-content">
                    <i class="material-icons blue-font clickable" onClick={() => console.log("save")}>save</i>
                    <i class="material-icons red-font clickable" onClick={() => console.log("delete")}>delete</i>
                </span>
            </li>

            <li class="collection-item"><div>&nbsp;<a href="#!" class="secondary-content"><i class="material-icons green-font">add</i></a></div></li>
        </ul>
        <ul class="collection with-header">
            <li class="collection-header"><h4>Trigger Name</h4></li>

            <li class="collection-item avatar">
                <i class="material-icons circle">folder</i>
                <span class="title">Title</span>
                <p><input type="text" placeholder="Delai (secondes)"></input></p>
                <span href="#!" class="secondary-content">
                    <i class="material-icons blue-font clickable" onClick={() => console.log("save")}>save</i>
                    <i class="material-icons red-font clickable" onClick={() => console.log("delete")}>delete</i>
                </span>
            </li>
            <li class="collection-item avatar">
                <i class="material-icons circle">folder</i>
                <span class="title">Title</span>
                <p><input type="text" placeholder="Delai (secondes)"></input></p>
                <span href="#!" class="secondary-content">
                    <i class="material-icons blue-font clickable" onClick={() => console.log("save")}>save</i>
                    <i class="material-icons red-font clickable" onClick={() => console.log("delete")}>delete</i>
                </span>
            </li>

            <li class="collection-item"><div>&nbsp;<a href="#!" class="secondary-content"><i class="material-icons green-font">add</i></a></div></li>
        </ul>
    </div>
}