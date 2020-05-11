import React from 'react';
import { useEffect, useState } from 'react';
import { StepperComponent, StepperDriver } from '../../commons/Stepper';
import FilesToBeSortedComponent from './FilesToBeSortedComponent'
import { todoSubEventReactor } from './TodoSubEventReactor';
import ChooseTargetDirectoryComponent from './ChooseTargetDirectoryComponent';
import RenameFilesComponent from './RenameFilesComponent';
import { actionMenu } from '../../commons/ActionMenuComponent';
import on from '../../../tools/on';
import ResumeMoveComponent from './ResumeMoveComponent';
import seedboxApi from '../../../apiManagement/SeedboxApi';

export default function TodoComponent() {
    const [moveRequest, setMoveRequest] = useState({ filesToMove: [], target: {} });

    const [stepperDriver, setStepperDriver] = useState(new StepperDriver()
        .appendStep("new_releases", "Fichiers à trier", <FilesToBeSortedComponent></FilesToBeSortedComponent>)
        .appendStep("gps_fixed", "Cibler", <ChooseTargetDirectoryComponent></ChooseTargetDirectoryComponent>)
        .appendStep("edit", "Renommer", <RenameFilesComponent></RenameFilesComponent>)
        .appendStep("done", "Appliquer", <ResumeMoveComponent moveRequest={moveRequest}></ResumeMoveComponent>)
        .withSelected(0)
        .disableAfterSelected()
        .disableNextStepButton());


    useEffect(() => {
        const unsubscribeFileSelectionChange = todoSubEventReactor().onFileSelectionChange(files => {

            if (files.length > 0) {
                actionMenu().open();
                setStepperDriver({ ...stepperDriver.enableNextStepButton() });
            } else {
                setStepperDriver({ ...stepperDriver.disableNextStepButton().disableAfterSelected() });
            }
            const newMoveRequest = {
                ...moveRequest,
                filesToMove: files
            };

            todoSubEventReactor().moveRequestChanged(newMoveRequest);


            setMoveRequest(newMoveRequest);
        });

        const unsubscribeUpdateDriver = stepperDriver.onDriverUpdated((newDriver) => {
            actionMenu().open();
            setStepperDriver({ ...newDriver });
        });

        const unsubscribeTargetSelectionChange = todoSubEventReactor().onTargetSelectionChange(newTarget => {

            if (newTarget) {
                actionMenu().open();
                setStepperDriver({ ...stepperDriver.enableNextStepButton() });
            } else {
                setStepperDriver({ ...stepperDriver.disableNextStepButton().disableAfterSelected() });
            }

            const newMoveRequest = {
                ...moveRequest,
                target: newTarget
            }
            todoSubEventReactor().moveRequestChanged(newMoveRequest);

            setMoveRequest(newMoveRequest);

        });

        const unsubscribeFileRename = todoSubEventReactor().onFileRenaming(oneFileRename => {
            const newMoveRequest = {
                ...moveRequest,
                filesToMove: moveRequest.filesToMove.map(oneFile => {
                    if (oneFile.id === oneFileRename.id) {
                        oneFile.newName = oneFileRename.newName;
                    }
                    return oneFile;
                })
            };

            if (newMoveRequest.filesToMove.filter(on().emptyString(oneFile => oneFile.newName)).length > 0) {
                setStepperDriver({ ...stepperDriver.disableNextStepButton().disableAfterSelected() });
            } else {
                setStepperDriver({ ...stepperDriver.enableNextStepButton() });
            }


            todoSubEventReactor().moveRequestChanged(newMoveRequest);
            setMoveRequest(newMoveRequest);
            todoSubEventReactor().renamedFile(newMoveRequest.filesToMove);
        });

        const unsubscribeStepperCompleted = stepperDriver.onComplete(() => {

            seedboxApi().moveFiles(moveRequest).then(() => {
                const newMoveRequest = { filesToMove: [], target: {} };
                setMoveRequest(newMoveRequest);
                todoSubEventReactor().moveRequestChanged(newMoveRequest);
    
    
                setStepperDriver({
                    ...stepperDriver.withSelected(0).disableAfterSelected()
                });

                todoSubEventReactor().moveRequestSuccessFull();
            });

            
        });


        return () => {
            unsubscribeFileSelectionChange();
            unsubscribeUpdateDriver();
            unsubscribeTargetSelectionChange();
            unsubscribeFileRename();
            unsubscribeStepperCompleted();
        }
    }, [moveRequest, stepperDriver]);


    return (
        <StepperComponent driver={stepperDriver}>
        </StepperComponent>
    )
}