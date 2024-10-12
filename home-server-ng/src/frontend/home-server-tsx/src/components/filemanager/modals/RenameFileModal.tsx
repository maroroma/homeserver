import {FC, useEffect, useState} from "react"
import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import {Form, Modal} from "react-bootstrap";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import PassiveBlockingButton from "../../blockingbutton/PassiveBlockingButton";
import {BootstrapVariants} from "../../bootstrap/BootstrapVariants";
import BlockingButton from "../../blockingbutton/BlockingButton";
import RenameFileDescriptor from "../../../model/filemanager/RenameFileDescriptor";
import FileManagerRequester from "../../../api/FileManagerRequester";
import LoadedDirectoryAction from "../../../context/actions/filemanager/LoadedDirectoryAction";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import {PencilSquare} from "react-bootstrap-icons";
import {CustomClassNames} from "../../bootstrap/CssTools";


export type RenameFileModalProps = {
    fileToRename: FileDescriptor,
    parentDirectory: FileDescriptor,
    show: boolean,
    onHide: () => void
}

const RenameFileModal: FC<RenameFileModalProps> = ({ show, fileToRename, onHide, parentDirectory }) => {

    const { workInProgress, dispatch } = useHomeServerContext();

    const [renameFile, setRenameFile] = useState(RenameFileDescriptor.empty())
    const [isValid, setIsValid] = useState(true);

    const updateNewName = (newName: string) => {
        setRenameFile({
            ...renameFile,
            newName: newName
        });

        setIsValid(newName !== "");
    }

    const launchRenameFile = () => {
        FileManagerRequester.renameFile(renameFile)
            .then(response => FileManagerRequester.getDirectoryDetails(parentDirectory))
            .then(response => {
                dispatch(new LoadedDirectoryAction(response, "Fichier renommé"))
                onHide();
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors du renommage du fichier")));
    }

    useEffect(() => {
        setRenameFile({
            newName: fileToRename.name,
            originalFile: fileToRename
        });
        setIsValid(true);
    }, [fileToRename]);


    return <Modal show={show} fullscreen={true} onHide={() => onHide()}>
        <Modal.Header closeButton={!workInProgress}>
            <Modal.Title className="text-break-anywhere">
                <PencilSquare className={CustomClassNames.SpaceAfterIcon} />
                Renommage du fichier <strong>{renameFile.originalFile.name}</strong>
            </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form.Control
                placeholder="nom obligatoire"
                value={renameFile.newName}
                onChange={(event) => updateNewName(event.target.value)}
                disabled={workInProgress}
                isValid={isValid}
                isInvalid={!isValid} />
        </Modal.Body>
        <Modal.Footer>
            <PassiveBlockingButton variant={BootstrapVariants.Secondary} onClick={() => onHide()} label="Annuler" />
            <BlockingButton
                label="Renommer"
                variant={BootstrapVariants.Primary}
                onClick={() => launchRenameFile()}
                disabled={!isValid}
                toastMessage="Renommage en cours"
            />
        </Modal.Footer>
    </Modal>
}

export default RenameFileModal;