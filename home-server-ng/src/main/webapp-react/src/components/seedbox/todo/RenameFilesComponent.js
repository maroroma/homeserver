import React from 'react';
import { useEffect, useState } from 'react';
import { todoSubEventReactor } from './TodoSubEventReactor';
import sort from '../../../tools/sort';
import { when } from '../../../tools/when';

export default function RenameFilesComponent() {

    const [filesToBeRename, setFilesToBeRename] = useState([]);


    useEffect(() => {
        const unSubscribeFileSelectionChange = todoSubEventReactor().onFileSelectionChange(files => {
            const filesWithNewNameField = files.map(oneFile => {
                if (oneFile.newName === undefined) {
                    oneFile.newName = oneFile.name;
                }
                return oneFile;
            }).sort(sort().fileName());

            setFilesToBeRename(filesWithNewNameField);
        });

        const unSubscribeFileRenamed = todoSubEventReactor().onFileRenamed(files => {
            setFilesToBeRename(files);
        });

        return () => {
            unSubscribeFileSelectionChange();
            unSubscribeFileRenamed();
        }
    }, []);

    const sendRenamingEvent = (file, newName) => {
        todoSubEventReactor().renamingFile({
            ...file,
            newName: newName
        });
    }

    const noDiff = (oneFile) => () => oneFile.newName === oneFile.name;

    return (
        <div>
            {filesToBeRename.map((oneFile, index) =>
                <div className="row" key={index}>
                    <div className="col s9">
                        <input id={oneFile.id} type="text" className="validate"
                            value={oneFile.newName}
                            placeholder={oneFile.name}
                            onChange={(event) => sendRenamingEvent(oneFile,
                                event.target.value)} />
                    </div>
                    <div className="col s3">
                        <a href="#!" className={when(noDiff(oneFile)).thenDisableElement("btn prefix")} onClick={() => sendRenamingEvent(oneFile, oneFile.name)}>
                            <i className="material-icons">undo</i>
                        </a>
                    </div>
                </div>)
            }
        </div>
    );

}