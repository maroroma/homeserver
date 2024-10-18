import {FC, useEffect, useState} from "react";
import SimpleMarginLayout from "../layouts/SimpleMarginLayout";
import {Accordion, AccordionItem, Form, InputGroup, ListGroup, ListGroupItem} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";
import SeedboxRequester from "../../api/SeedboxRequester";
import TodoFilesLoadedAction from "../../context/actions/seedbox/TodoFilesLoadedAction";
import FileDescriptorRenderer from "../filemanager/FileDescriptorRenderer";
import FileDescriptorIconResolver from "../../model/filemanager/FileDescriptorIconResolver";
import SelectableItems from "../../model/SelectableItems";
import FileDescriptor from "../../model/filemanager/FileDescriptor";
import SwitchSelectTodoFileAction from "../../context/actions/seedbox/SwitchSelectTodoFileAction";
import SelectableItem from "../../model/SelectableItem";
import {TodoSteps} from "../../model/seedbox/TodoSteps";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionBackButton from "../actionmenu/ActionBackButton";
import ActionNextButton from "../actionmenu/ActionNextButton";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import TodoNextStepAction from "../../context/actions/seedbox/TodoNextStepAction";
import EndWIPAction from "../../context/actions/EndWIPAction";
import TodoTargetsLoadedAction from "../../context/actions/seedbox/TodoTargetsLoadedAction";
import {BootstrapText} from "../bootstrap/BootstrapText";
import DirectoryDescriptorIconResolver from "../../model/filemanager/DirectoryDescriptorIconResolver";
import BackIconResolver from "../../model/filemanager/BackIconResolver";
import FileDirectoryDescriptor from "../../model/filemanager/FileDirectoryDescriptor";
import TodoSubTargetsLoadedAction from "../../context/actions/seedbox/TodoSubTargetsLoadedAction";
import TodoRenameFileAction from "../../context/actions/seedbox/TodoRenameFileAction";
import {FolderPlus, PencilSquare} from "react-bootstrap-icons";
import ActionCheckButton from "../actionmenu/ActionCheckButton";
import CssTools from "../bootstrap/CssTools";
import FileToMoveDescriptor from "../../model/seedbox/FileToMoveDescriptor";
import MoveRequest from "../../model/seedbox/MoveRequest";
import ToastAction from "../../context/actions/ToastAction";
import DirectoryCreationRequest from "../../model/filemanager/DirectoryCreationRequest";
import BlockingButton from "../blockingbutton/BlockingButton";
import FileManagerRequester from "../../api/FileManagerRequester";
import IfComponent from "../layouts/IfComponent";

const SeedBoxTodoComponent: FC = () => {


    const { seedboxTodoSubState, dispatch, searchString, workInProgress } = useHomeServerContext();
    const [filteredTodoFiles, setFilteredTodoFiles] = useState<SelectableItems<FileDescriptor>>(SelectableItems.empty());
    const [directoryToAdd, setDirectoryToAdd] = useState(DirectoryCreationRequest.empty())


    useEffect(() => {

        if (seedboxTodoSubState.todoFiles.items.length === 0) {
            dispatch(new StartWIPAction("Chargement des fichiers à trier"));
            SeedboxRequester.getTodoList()
                .then(response => {
                    dispatch(new TodoFilesLoadedAction(response));
                    dispatch(new StartWIPAction("Chargement des cibles"));
                    return SeedboxRequester.getTargetDirectories();
                })
                .then(response => {
                    dispatch(new EndWIPAction());
                    dispatch(new TodoTargetsLoadedAction(response));
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la préparation des données")))
        }

    }, [seedboxTodoSubState])

    useEffect(() => {
        setFilteredTodoFiles(seedboxTodoSubState.todoFiles.filter(FileDescriptor.filter(searchString)))
    }, [seedboxTodoSubState.todoFiles, searchString])

    useEffect(() => {
        setDirectoryToAdd({
            ...directoryToAdd,
            parentDirectory: seedboxTodoSubState.currentTargetDirectory
        })

    }, [seedboxTodoSubState.currentTargetDirectory])

    const switchTodoFileSelection = (todoFile: SelectableItem<FileDescriptor>) => {
        dispatch(new SwitchSelectTodoFileAction(todoFile))
    }

    const addNewDirectory = () => {
        FileManagerRequester.addNewDirectory(directoryToAdd)
            .then(response =>
                SeedboxRequester.getSubTargetDirectories(seedboxTodoSubState.currentTargetDirectory)
            )
            .then(response => {
                setDirectoryToAdd(DirectoryCreationRequest.empty());
                dispatch(new EndWIPAction());
                dispatch(new TodoSubTargetsLoadedAction(response));
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de l'ajout du nouveau répertoire")))
    }

    const loadSubDirectories = (directoryToLoad: FileDescriptor) => {
        dispatch(new StartWIPAction("Chargement du sous répertoire cible"));

        SeedboxRequester.getSubTargetDirectories(directoryToLoad)
            .then(response => {
                dispatch(new EndWIPAction());
                dispatch(new TodoSubTargetsLoadedAction(response));
            })
            .catch(error => new EndWIPInErrorAction("Erreur rencontrée lors du chargement du sous répertoire cible"));
    }

    const goBack = () => {
        if ((seedboxTodoSubState.targetDirectoriesStack.length - 2) > 0) {
            loadSubDirectories(seedboxTodoSubState.targetDirectoriesStack[seedboxTodoSubState.targetDirectoriesStack.length - 2])
        } else {
            SeedboxRequester.getTargetDirectories()
                .then(response => dispatch(new TodoTargetsLoadedAction(response)))
        }
    }

    const sendMoveRequest = () => {
        const moveRequest = new MoveRequest(seedboxTodoSubState.filesToMove, seedboxTodoSubState.currentTargetDirectory);


        SeedboxRequester.moveFiles(moveRequest)
            .then(response => {
                dispatch(ToastAction.info("Déplacement terminé"));
                return SeedboxRequester.getTodoList()
            })
            .then(response => {
                dispatch(new TodoFilesLoadedAction(response));
                dispatch(new StartWIPAction("Chargement des cibles"));
                return SeedboxRequester.getTargetDirectories();
            })
            .then(response => {
                dispatch(new EndWIPAction());
                dispatch(new TodoTargetsLoadedAction(response));
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du déplacement ou de la récupération des nouvelles données")))
    }

    return <SimpleMarginLayout>
        <Accordion activeKey={seedboxTodoSubState.currentStep}>


            <AccordionItem eventKey={TodoSteps.SELECT_TODO_FILES}>
                <Accordion.Header className={BootstrapText.UpperCase}>SELECTION</Accordion.Header>
                <Accordion.Body>
                    <ListGroup>
                        {filteredTodoFiles.items.map((selectableTodoFile, index) => <FileDescriptorRenderer
                            key={index}
                            fileIconResolver={new FileDescriptorIconResolver()}
                            selectableFileDescriptor={selectableTodoFile}
                            onCheck={() => { switchTodoFileSelection(selectableTodoFile) }}
                            onClick={() => { switchTodoFileSelection(selectableTodoFile) }}
                        />
                        )}
                    </ListGroup>
                </Accordion.Body>
            </AccordionItem>


            <AccordionItem eventKey={TodoSteps.SELECT_TARGET}>
                <Accordion.Header>CIBLE</Accordion.Header>
                <Accordion.Body>
                    <ListGroup>
                        <IfComponent hideIf={FileDirectoryDescriptor.isRoot(seedboxTodoSubState.currentTargetDirectory)}>
                            <FileDescriptorRenderer
                                fileIconResolver={new BackIconResolver()}
                                selectableFileDescriptor={new SelectableItem(FileDescriptor.named(".."), false)}
                                onCheck={() => { }}
                                onClick={() => { goBack() }}
                            />
                        </IfComponent>
                        {seedboxTodoSubState.currentTargetDirectory.directories.map((targetDirectory, index) => <FileDescriptorRenderer
                            key={index}
                            fileIconResolver={new DirectoryDescriptorIconResolver()}
                            selectableFileDescriptor={new SelectableItem(targetDirectory, false)}
                            onCheck={() => { }}
                            onClick={() => { loadSubDirectories(targetDirectory) }}
                        />
                        )}
                        <IfComponent hideIf={FileDirectoryDescriptor.isRoot(seedboxTodoSubState.currentTargetDirectory)}>
                            <ListGroupItem>
                                <InputGroup className="mb-3">
                                    <Form.Control
                                        disabled={workInProgress}
                                        id="basic-url"
                                        aria-describedby="basic-addon3"
                                        value={directoryToAdd.directoryName}
                                        onChange={(event) => setDirectoryToAdd({ ...directoryToAdd, directoryName: event.target.value })}
                                    />
                                    <BlockingButton
                                        icon={<FolderPlus />}
                                        disabled={directoryToAdd.directoryName === undefined || directoryToAdd.directoryName === ""}
                                        label=""
                                        onClick={() => addNewDirectory()}
                                        toastMessage="Ajout d'un nouveau répertoire"
                                    />
                                </InputGroup>
                            </ListGroupItem>
                        </IfComponent>
                    </ListGroup>
                </Accordion.Body>
            </AccordionItem>

            <AccordionItem eventKey={TodoSteps.RENAME_TODO_FILES}>
                <Accordion.Header>RENOMMER</Accordion.Header>
                <Accordion.Body>
                    {seedboxTodoSubState.filesToMove.map((aFileToMove, renameFileIndex) => <Form.Control
                        className={CssTools.of("").if(FileToMoveDescriptor.isRenamed(aFileToMove), BootstrapText.ColorWarning).css()}
                        key={renameFileIndex}
                        placeholder={aFileToMove.name}
                        value={aFileToMove.newName}
                        onChange={(event) => dispatch(new TodoRenameFileAction(aFileToMove, event.target.value))}
                    />)}
                </Accordion.Body>
            </AccordionItem>
            <AccordionItem eventKey={TodoSteps.MOVE_SYNTHESIS}>
                <Accordion.Header>SYNTHESE</Accordion.Header>
                <Accordion.Body>
                    <div>
                        <h3 className={BootstrapText.ColorSuccess}>Cible</h3>
                        <h2 className={BootstrapText.ColorLight}>{seedboxTodoSubState.currentTargetDirectory.fullName}</h2>
                    </div>
                    <div>
                        <h3 className={BootstrapText.ColorSuccess}>Fichiers</h3>
                        <ListGroup>
                            {seedboxTodoSubState.filesToMove.map((aFileToMove, index) => <ListGroupItem key={index}>
                                <h4 className={BootstrapText.ColorLight}>
                                    {FileToMoveDescriptor.isRenamed(aFileToMove) ? <PencilSquare className={BootstrapText.ColorWarning} /> : <></>} {aFileToMove.newName}
                                </h4>
                                {FileToMoveDescriptor.isRenamed(aFileToMove) ? <h6 className={BootstrapText.ColorWarning}>{`(${aFileToMove.name})`}</h6>
                                    : <></>
                                }
                            </ListGroupItem>)}
                        </ListGroup>
                    </div>

                </Accordion.Body>
            </AccordionItem>

        </Accordion>
        <ActionMenuComponent alreadyOpen>
            <ActionBackButton
                disabled={seedboxTodoSubState.backButtonDisabled}
                onClick={() => dispatch(TodoNextStepAction.previous())}
            />
            {
                seedboxTodoSubState.currentStep === TodoSteps.MOVE_SYNTHESIS ?
                    <ActionCheckButton
                        toastMessage="Déplacement des fichiers"
                        blockingButton={true}
                        onClick={() => sendMoveRequest()}
                    />
                    : <ActionNextButton
                        disabled={seedboxTodoSubState.nextButtonDisabled}
                        onClick={() => dispatch(TodoNextStepAction.next())}
                    />
            }

        </ActionMenuComponent>

    </SimpleMarginLayout>
}

export default SeedBoxTodoComponent;