import React from 'react';
import IconComponent from '../commons/IconComponent';
import CheckBoxComponent from '../commons/CheckBoxComponent';
import eventReactor from '../../eventReactor/EventReactor';
import {fileIconResolver} from "./FileIconResolver"
import { when } from '../../tools/when';


export default function FileRenderer({ file, iconResolver = fileIconResolver }) {

    const onChangeSelectionEventHander = (newStatus) => {
        eventReactor().shortcuts().selectItem(file.id, newStatus);
    }

    return <li className="collection-item">
        <div>
            <CheckBoxComponent dataswitch={file.selected} onChange={onChangeSelectionEventHander}></CheckBoxComponent>
            <a className="waves-effect waves-teal btn-flat file-item" href="#!" onClick={() => eventReactor().shortcuts().selectItem(file.id, !file.selected)}>
                <IconComponent icon={iconResolver(file)} classAddons={when(file.selected).thenPlopSelectedItem("left")}></IconComponent>
                <span className="truncate">
                    {file.name}
                </span>
            </a>
        </div>
    </li>;

}