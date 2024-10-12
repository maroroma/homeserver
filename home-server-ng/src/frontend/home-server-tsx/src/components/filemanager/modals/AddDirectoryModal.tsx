import {FC, useEffect, useState} from "react"
import {Form, Modal} from "react-bootstrap";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import PassiveBlockingButton from "../../blockingbutton/PassiveBlockingButton";
import {BootstrapVariants} from "../../bootstrap/BootstrapVariants";
import BlockingButton from "../../blockingbutton/BlockingButton";
import FileManagerRequester from "../../../api/FileManagerRequester";
import LoadedDirectoryAction from "../../../context/actions/filemanager/LoadedDirectoryAction";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import {PlusCircle} from "react-bootstrap-icons";
import {CustomClassNames} from "../../bootstrap/CssTools";
import DirectoryCreationRequest from "../../../model/filemanager/DirectoryCreationRequest";
import FileDirectoryDescriptor from "../../../model/filemanager/FileDirectoryDescriptor";


export type AddDirectoryModalProps = {
    parentDirectory: FileDirectoryDescriptor,
    show: boolean,
    onHide: () => void
}

const AddDirectoryModal: FC<AddDirectoryModalProps> = ({ show, onHide, parentDirectory }) => {

    const { workInProgress, dispatch } = useHomeServerContext();

    const [directoryToAdd, setDirectoryToAdd] = useState(DirectoryCreationRequest.empty())
    const [isValid, setIsValid] = useState(true);

    const updateNewName = (newName: string) => {
        setDirectoryToAdd({
            ...directoryToAdd,
            directoryName: newName
        });

        setIsValid(newName !== "");
    }

    useEffect(() => {
        setDirectoryToAdd({
            ...directoryToAdd,
            parentDirectory: parentDirectory,
            directoryName: ""
        });

    }, [parentDirectory])

    const launchDirectoryCreation = () => {
        FileManagerRequester.addNewDirectory(directoryToAdd)
            .then(response => FileManagerRequester.getDirectoryDetails(parentDirectory))
            .then(response => {
                dispatch(new LoadedDirectoryAction(response, "Dossier créé"))
                onHide();
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors de la création du répertoire")));
    }


    return <Modal show={show} fullscreen={true} onHide={() => onHide()}>
        <Modal.Header closeButton={!workInProgress}>
            <Modal.Title className="text-break-anywhere">
                <PlusCircle className={CustomClassNames.SpaceAfterIcon} />
                Ajout d'un nouveau répertoire
            </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form.Control
                placeholder="nom obligatoire"
                value={directoryToAdd.directoryName}
                onChange={(event) => updateNewName(event.target.value)}
                disabled={workInProgress}
                isValid={isValid}
                isInvalid={!isValid} />
        </Modal.Body>
        <Modal.Footer>
            <PassiveBlockingButton variant={BootstrapVariants.Secondary} onClick={() => onHide()} label="Annuler" />
            <BlockingButton
                label="Créer"
                variant={BootstrapVariants.Primary}
                onClick={() => launchDirectoryCreation()}
                disabled={!isValid}
                toastMessage="Ajout en cours"
            />
        </Modal.Footer>
    </Modal>
}

export default AddDirectoryModal;