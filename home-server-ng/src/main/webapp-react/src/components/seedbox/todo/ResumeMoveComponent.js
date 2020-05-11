import React from 'react';
import { useEffect, useState } from 'react';
import IconComponent from '../../commons/IconComponent';
import { fileIconResolver } from '../../filemanager/FileIconResolver';
import { todoSubEventReactor } from './TodoSubEventReactor';

export default function ResumeMoveComponent() {

    const [moveRequest, setMoveRequest] = useState({ filesToMove: [], target: {} });

    useEffect(() => todoSubEventReactor().onMoveRequestChanged(newMoveRequest => setMoveRequest(newMoveRequest)), []);

    return <ul className="collection with-header">
        <li className="collection-header"><h4>Fichiers Sélectionnés</h4></li>
        {moveRequest.filesToMove.map((oneFile, index) => {

            const fileNameToDisplay = (oneFile.name === oneFile.newName) ? oneFile.newName : `${oneFile.newName} (depuis ${oneFile.name})`;


            return <li className="collection-item" key={index}>
                <IconComponent icon={fileIconResolver(oneFile)} classAddons="left"></IconComponent>
                <span className="truncate">
                    {fileNameToDisplay}
                </span></li>
        }
        )}
        <li className="collection-header"><h4>Cible</h4></li>
        <li className="collection-item">
            <IconComponent icon="folder" classAddons="left"></IconComponent>
            <span className="truncate">{moveRequest.target?.name}</span>
        </li>
    </ul>;
}