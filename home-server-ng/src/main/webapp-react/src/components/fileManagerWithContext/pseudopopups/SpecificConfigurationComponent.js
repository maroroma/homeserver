import React, {useEffect, useState} from 'react';
import {useDisplayList} from '../../../tools/displayList';
import {administrationApi} from '../../../apiManagement/AdministrationApi';
import {ConfigurationProperties} from '../../administration/ConfigurationProperties';
import {useFileBrowserContext} from '../FileBrowserContextDefinition';
import {when} from '../../../tools/when';
import sort from '../../../tools/sort';

export default function SpecificConfigurationComponent() {

    const { dispatchEndConfiguring } = useFileBrowserContext();

    const [rootDirectories, setRootDirectories] = useDisplayList();


    const [initialValue, setInitialValue] = useState("");
    const [newValue, setNewValue] = useState("");

    const [newRootDirectory, setNewRootDirectory] = useState("");
    const [enableAddNewDirectory, setEnableAddNewDirectory] = useState(false);
    const [enableSaveProperty, setEnableSaveProperty] = useState(false);

    useEffect(() => {

        administrationApi()
            .getOneProperty(ConfigurationProperties.ROOT_DIRECTORIES)
            .then(oneProperty => {

                const initialSortedValue = oneProperty.value.split(",").sort(sort().basic()).join(",");

                setInitialValue(initialSortedValue);
                setNewValue(initialSortedValue);
                setEnableAddNewDirectory(false);

                setRootDirectories({
                    ...rootDirectories
                        .update(oneProperty.value.split(","))
                        .updateSort(sort().basic())
                });
            });

    }, []);

    useEffect(() => {
        setEnableSaveProperty(initialValue !== newValue);
    }, [newValue, initialValue]);

    const deleteOneDirectory = (directory) => {
        const newRawList = rootDirectories.rawList.filter(aRootDirectory => aRootDirectory !== directory);

        setNewValue(newRawList.sort(sort().basic).join(","));

        setRootDirectories({ ...rootDirectories.update(newRawList).updateSort(sort().basic()) });

    }

    const addARootDirectory = () => {
        if (newRootDirectory !== "") {
            const newRawList = [...rootDirectories.rawList];
            newRawList.push(newRootDirectory);

            setNewValue(newRawList.sort(sort().basic).join(","));

            setRootDirectories({ ...rootDirectories.update(newRawList).updateSort(sort().basic()) });

            setNewRootDirectory("");
        }
    }


    useEffect(() => {
        setEnableAddNewDirectory(newRootDirectory !== "");
    }, [newRootDirectory]);


    const saveProperty = () => {
        administrationApi().updateProperties([{
            id: ConfigurationProperties.ROOT_DIRECTORIES,
            value: newValue
        }]).then(result => dispatchEndConfiguring());

    }




    return <div>
        <div>
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
        </div>
    </div>
}