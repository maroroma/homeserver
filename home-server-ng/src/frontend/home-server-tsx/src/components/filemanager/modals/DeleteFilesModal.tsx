import {FC} from "react"
import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import {ListGroup, ListGroupItem, Modal} from "react-bootstrap";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import PassiveBlockingButton from "../../blockingbutton/PassiveBlockingButton";
import {BootstrapVariants} from "../../bootstrap/BootstrapVariants";
import BlockingButton from "../../blockingbutton/BlockingButton";
import FileManagerRequester from "../../../api/FileManagerRequester";
import LoadedDirectoryAction from "../../../context/actions/filemanager/LoadedDirectoryAction";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import {Trash} from "react-bootstrap-icons";
import {CustomClassNames} from "../../bootstrap/CssTools";


export type DeleteFilesModalProps = {
    filesToDelete: FileDescriptor[],
    parentDirectory: FileDescriptor,
    show: boolean,
    onHide: () => void
}

const DeleteFilesModal: FC<DeleteFilesModalProps> = ({ show, filesToDelete, onHide, parentDirectory }) => {

    const { workInProgress, dispatch } = useHomeServerContext();

    const launchDeleteFiles = () => {
        FileManagerRequester.deleteFiles(filesToDelete)
            .then(response => FileManagerRequester.getDirectoryDetails(parentDirectory))
            .then(response => {
                dispatch(new LoadedDirectoryAction(response, "Fichiers supprimés"))
                onHide();
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors de la suppression des fichiers")));
    }

    return <Modal show={show} fullscreen={true} onHide={() => onHide()}>
        <Modal.Header closeButton={!workInProgress}>
            <Modal.Title className="text-break-anywhere">
                <Trash className={CustomClassNames.SpaceAfterIcon}/>
                {`Supprimer les ${filesToDelete.length} fichiers suivants ?`}
            </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <ListGroup>
                {filesToDelete.map((aFileToDelete, index) => <ListGroupItem key={index}>{aFileToDelete.name}</ListGroupItem>)}
            </ListGroup>
        </Modal.Body>
        <Modal.Footer>
            <PassiveBlockingButton variant={BootstrapVariants.Primary} onClick={() => onHide()} label="Annuler" />
            <BlockingButton
                label="Supprimer"
                variant={BootstrapVariants.Danger}
                onClick={() => launchDeleteFiles()}
                toastMessage="Suppression en cours"
                icon={<Trash />}
            />
        </Modal.Footer>
    </Modal>
}

export default DeleteFilesModal;