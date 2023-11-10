import React, {useEffect, useState} from 'react';
import {useDisplayList} from '../../../tools/displayList';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import {when} from '../../../tools/when';
import sort from '../../../tools/sort';
import fileManagerApi from '../../../apiManagement/FileManagerApi';

import "./SpecificConfigurationComponent.scss";

export default function SpecificConfigurationComponent() {

    const { dispatchEndConfiguring } = useFileBrowserContext();


    const [rootDirectoriesConfiguration, setRootDirectoriesConfiguration] = useDisplayList();
    const [newRootDirectoryConfigurationPath, setNewRootDirectoryConfigurationPath] = useState("");
    const [enableAddNewDirectoryConfiguration, setableAddNewDirectoryConfiguration] = useState(false);


    useEffect(() => {

        fileManagerApi()
            .getRootDirectoriesConfiguration()
            .then(result => {
                setRootDirectoriesConfiguration({ ...rootDirectoriesConfiguration
                    .update(result)
                    .updateSort(sort().basic(aConfig => aConfig.rawPath)) })
            });

    }, []);

    useEffect(() => {
        setableAddNewDirectoryConfiguration(newRootDirectoryConfigurationPath !== "");
    }, [newRootDirectoryConfigurationPath]);


    const addNewDirectoryConfiguration = () => {

        if (enableAddNewDirectoryConfiguration) {

        fileManagerApi()
            .addNewDirectoryConfiguration(newRootDirectoryConfigurationPath)
            .then(result => {
                setNewRootDirectoryConfigurationPath("");
                setRootDirectoriesConfiguration({ ...rootDirectoriesConfiguration.update(result) });
            });
        }
    }

    const changeVisibility = (aRootDirectoryConfiguration) => {
        const configurationToUpdate = {
            ...aRootDirectoryConfiguration,
            hidden: !aRootDirectoryConfiguration.hidden
        }

        fileManagerApi().updateRootDirectoryConfiguration(configurationToUpdate)
            .then(result => setRootDirectoriesConfiguration({ ...rootDirectoriesConfiguration.update(result) }));
    };

    const changeSecured = (aRootDirectoryConfiguration) => {
        const configurationToUpdate = {
            ...aRootDirectoryConfiguration,
            protected: !aRootDirectoryConfiguration.protected
        }

        fileManagerApi().updateRootDirectoryConfiguration(configurationToUpdate)
            .then(result => setRootDirectoriesConfiguration({ ...rootDirectoriesConfiguration.update(result) }));
    };

    const deleteConfiguration = (aRootDirectoryConfiguration) => {
        fileManagerApi().deleteRootDirectoryConfiguration(aRootDirectoryConfiguration)
            .then(result => setRootDirectoriesConfiguration({ ...rootDirectoriesConfiguration.update(result) }));
    }





    return <div>
        <div>
            <h4 className="pseudo-popup-header">Répertoires racine</h4>
        </div>
        <div>
            <ul className='collection'>
                {rootDirectoriesConfiguration.displayList.map((aRootDirectoryConfiguration, key) => <li className='collection-item root-directory-item' key={key}>
                    {aRootDirectoryConfiguration.rawPath}
                    <a href="#!" className="secondary-content" onClick={() => { deleteConfiguration(aRootDirectoryConfiguration) }}>
                        <i className="material-icons red-font">delete</i>
                    </a>
                    <a href="#!" className="secondary-content" onClick={() => { changeSecured(aRootDirectoryConfiguration) }}>
                        {aRootDirectoryConfiguration.protected
                            ? <i className="material-icons orange-text">lock</i>
                            : <i className="material-icons green-font">lock_open</i>}
                    </a>
                    <a href="#!" className="secondary-content" onClick={() => { changeVisibility(aRootDirectoryConfiguration) }}>
                        {aRootDirectoryConfiguration.hidden
                            ? <i className="material-icons orange-text">visibility_off</i>
                            : <i className="material-icons green-font">visibility</i>}
                    </a>
                </li>)}
                <li className='collection-item valign-wrapper'>
                    <input id="icon_prefix" type="text" className="validate input-new-directory" value={newRootDirectoryConfigurationPath} onChange={(event) => setNewRootDirectoryConfigurationPath(event.target.value)}></input>
                    <a href="#!"
                        disabled={!enableAddNewDirectoryConfiguration}
                        className={when(!enableAddNewDirectoryConfiguration).thenDisableElement("secondary-content")}
                        onClick={() => { addNewDirectoryConfiguration() }}>
                        <i className="material-icons">create_new_folder</i>
                    </a>
                </li>
            </ul>
        </div>
        <div className="right-align">
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons red"
                onClick={() => dispatchEndConfiguring()}>
                <i className="material-icons left">exit_to_app</i>
                Quitter
            </a>
        </div>

        {/* <div>
            <h4 className="pseudo-popup-header">Répertoires racine</h4>
        </div>
        <div>
            <ul className='collection'>
                {rootDirectories.displayList.map((aRootDirectory, key) => <li className='collection-item' key={key}>
                    {aRootDirectory}
                    <a href="#!" className="secondary-content" onClick={() => deleteOneDirectory(aRootDirectory)}>
                        <i className="material-icons red-font">delete</i>
                    </a>
                </li>)}
                <li className='collection-item valign-wrapper'>
                    <input id="icon_prefix" type="text" className="validate input-new-directory" value={newRootDirectory} onChange={(event) => setNewRootDirectory(event.target.value)}></input>
                    <a href="#!" disabled={!enableAddNewDirectory} className={when(!enableAddNewDirectory).thenDisableElement("secondary-content")} onClick={() => { addARootDirectory() }}>
                        <i className="material-icons">library_add</i>
                    </a>
                </li>
            </ul>
        </div>
        <div className="right-align">
            <a href="#!" className={when(!enableSaveProperty).thenDisableElement("waves-effect waves-light btn pseudo-popup-action-buttons blue")}
                onClick={() => { saveProperty() }}>
                <i className="material-icons left">save</i>
                Sauvegarder
            </a>
            <a href="#!" className="waves-effect waves-light btn pseudo-popup-action-buttons red"
                onClick={() => dispatchEndConfiguring()}>
                <i className="material-icons left">exit_to_app</i>
                Quitter
            </a>
        </div> */}
    </div>
}