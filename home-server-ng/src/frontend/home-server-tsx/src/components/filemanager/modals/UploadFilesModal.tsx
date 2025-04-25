import { FC, useState } from "react"
import FileDescriptor from "../../../model/filemanager/FileDescriptor";
import { Button, Form, InputGroup, ListGroup, ListGroupItem, Modal } from "react-bootstrap";
import { useHomeServerContext } from "../../../context/HomeServerRootContext";
import PassiveBlockingButton from "../../blockingbutton/PassiveBlockingButton";
import { BootstrapVariants } from "../../bootstrap/BootstrapVariants";
import BlockingButton from "../../blockingbutton/BlockingButton";
import FileManagerRequester from "../../../api/FileManagerRequester";
import LoadedDirectoryAction from "../../../context/actions/filemanager/LoadedDirectoryAction";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import { PlusCircle, Trash, Upload } from "react-bootstrap-icons";
import { CustomClassNames } from "../../bootstrap/CssTools";
import FileDescriptorIconResolver from "../../../model/filemanager/FileDescriptorIconResolver";
import { BootstrapText } from "../../bootstrap/BootstrapText";


export type UploadFilesModalProps = {
    parentDirectory: FileDescriptor,
    show: boolean,
    onHide: () => void
}

const UploadFilesModal: FC<UploadFilesModalProps> = ({ show, onHide, parentDirectory }) => {

    const { workInProgress, dispatch } = useHomeServerContext();

    const [filesToUpload, setFilesToUpload] = useState<File[]>([]);

    const [urlsToUpload, setUrlsToUpload] = useState<string[]>([]);

    const [urlToAdd, setUrlToAdd] = useState("");

    const fileIconResolver = new FileDescriptorIconResolver();

    const launchUploadFiles = () => {


        var promises = [];

        if (filesToUpload.length > 0) {
            promises.push(FileManagerRequester.uploadFiles(parentDirectory, filesToUpload));
        }
        
        if (urlsToUpload.length > 0) {
            promises.push(FileManagerRequester.uploadFilesFromUrl(parentDirectory, urlsToUpload));
        }

        Promise.all(promises)
            .then(response => FileManagerRequester.getDirectoryDetails(parentDirectory))
            .then(response => {
                dispatch(new LoadedDirectoryAction(response, "Fichiers uploadés"));
                setFilesToUpload([]);
                setUrlsToUpload([]);
                onHide();
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors de l'upload des fichiers")));





    }



    return <Modal show={show} fullscreen={true} onHide={() => onHide()}>
        <Modal.Header closeButton={!workInProgress}>
            <Modal.Title className="text-break-anywhere">
                <Upload className={CustomClassNames.SpaceAfterIcon} />
                {`Uploader les fichiers suivants`}
            </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Form.Group controlId="formFileMultiple" className="mb-3">
                <Form.Label>Sélectionner les fichier</Form.Label>
                <Form.Control type="file" multiple onChange={(event: any) => setFilesToUpload(Array.from(event.target.files))} />
            </Form.Group>
            <ListGroup>
                {filesToUpload?.map((aFileToUpload, index) => <ListGroupItem key={index}>
                    <h2>
                        {fileIconResolver.toIcon(FileDescriptor.named(aFileToUpload.name))}
                        {aFileToUpload.name}
                    </h2>
                </ListGroupItem>)}
            </ListGroup>

            <ListGroup data-bs-theme="light">
                {urlsToUpload.map(aTorrent => {
                    return <ListGroup.Item className={BootstrapText.AlignLeft}>
                        <h2>
                            {aTorrent}
                            <Button
                                variant={BootstrapVariants.Danger}
                                className={CustomClassNames.PullRight}
                                onClick={() => {
                                    setUrlsToUpload(urlsToUpload.filter(anUrl => anUrl !== aTorrent))

                                }}
                            >
                                <Trash />
                            </Button>
                        </h2>

                    </ListGroup.Item>
                })
                }
                <ListGroupItem>
                    <InputGroup className="mb-3">
                        <Form.Control
                            placeholder="Url à télécharger"
                            value={urlToAdd}
                            onChange={(event) => {
                                setUrlToAdd(event.target.value)
                            }}
                        />
                        <Button
                            onClick={() => {
                                setUrlsToUpload([...urlsToUpload, urlToAdd])
                                setUrlToAdd("")
                            }}
                            disabled={urlToAdd === ""}
                        >
                            <PlusCircle />
                            { }
                        </Button>
                    </InputGroup>
                </ListGroupItem>
            </ListGroup>
        </Modal.Body>
        <Modal.Footer>
            <PassiveBlockingButton variant={BootstrapVariants.Secondary} onClick={() => onHide()} label="Annuler" />
            <BlockingButton
                label="Uploader"
                variant={BootstrapVariants.Primary}
                onClick={() => launchUploadFiles()}
                toastMessage="Upload en cours"
                icon={<Upload />}
                disabled={filesToUpload.length === 0 && urlsToUpload.length === 0}
            />
        </Modal.Footer>
    </Modal>
}

export default UploadFilesModal;