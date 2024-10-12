import {FC, useEffect, useState} from "react"
import {ButtonGroup, Form, InputGroup, ListGroup, ListGroupItem, Modal} from "react-bootstrap";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import PassiveBlockingButton from "../../blockingbutton/PassiveBlockingButton";
import {BootstrapVariants} from "../../bootstrap/BootstrapVariants";
import BlockingButton from "../../blockingbutton/BlockingButton";
import FileManagerRequester from "../../../api/FileManagerRequester";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import {Eye, EyeSlash, Gear, Lock, PlusLg, Trash, Unlock} from "react-bootstrap-icons";
import CssTools, {CustomClassNames} from "../../bootstrap/CssTools";
import RootDirectoryConfiguration from "../../../model/filemanager/RootDirectoryConfiguration";

import "./RootDirectoriesModal.css";
import StartWIPAction from "../../../context/actions/StartWIPAction";
import EndWIPAction from "../../../context/actions/EndWIPAction";
import LoadedRootDirectoriesAction from "../../../context/actions/filemanager/LoadedRootDirectoriesAction";


export type RootDirectoriesModalProps = {
    show: boolean,
    onHide: () => void
}

const RootDirectoriesModal: FC<RootDirectoriesModalProps> = ({ show, onHide }) => {

    const { workInProgress, dispatch } = useHomeServerContext();

    const [rootDirectories, setRootDirectories] = useState<RootDirectoryConfiguration[]>([]);

    const [newRootDirectoryPath, setNewRootDirectoryPath] = useState("");


    useEffect(() => {
        FileManagerRequester.getRootDirectoriesConfiguration()
            .then(response => setRootDirectories(response.sort(RootDirectoryConfiguration.sorter())))
    }, []);

    const updateVisibility = (root: RootDirectoryConfiguration) => {
        updateRootDirectory(root, root => {
            return {
                ...root,
                hidden: !root.hidden
            }
        })
    }

    const updateProtected = (root: RootDirectoryConfiguration) => {
        updateRootDirectory(root, root => {
            return {
                ...root,
                isProtected: !root.isProtected
            }
        })
    }

    const updateRootDirectory = (root: RootDirectoryConfiguration, mapper: (rootDirectory: RootDirectoryConfiguration) => RootDirectoryConfiguration) => {
        FileManagerRequester.updateRootDirectoryConfiguration(mapper(root))
            .then(response => {
                dispatch(new EndWIPAction("Modification effectuée"))
                console.log(response)
                setRootDirectories(response.sort(RootDirectoryConfiguration.sorter()));
            })
            .catch(error => dispatch(new EndWIPInErrorAction("erreur survenue lors de l'update")))
    }

    const addNewDirectory = () => {
        FileManagerRequester.addRootDirectory(newRootDirectoryPath)
            .then(response => {
                dispatch(new EndWIPAction())
                console.log(response)
                setRootDirectories(response.sort(RootDirectoryConfiguration.sorter()));
            })
            .catch(error => dispatch(new EndWIPInErrorAction("erreur survenue lors de l'ajout")))
    }

    const deleteRootDirectory = (rootDirectory: RootDirectoryConfiguration) => {
        FileManagerRequester.deleteRootDirectory(rootDirectory)
            .then(response => {
                dispatch(new EndWIPAction())
                console.log(response)
                setRootDirectories(response.sort(RootDirectoryConfiguration.sorter()));
            })
            .catch(error => dispatch(new EndWIPInErrorAction("erreur survenue lors de la suppression")))
    }

    const leave = () => {
        dispatch(new StartWIPAction("Chargement des répertoires racines"));

        FileManagerRequester.getRootDirectories()
            .then(response => {
                dispatch(new LoadedRootDirectoriesAction(response))
                onHide();
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors du chargement des répertoires racines")))
    }


    return <Modal show={show} fullscreen={true} onHide={() => leave()}>
        <Modal.Header closeButton={!workInProgress}>
            <Modal.Title className="text-break-anywhere">
                <Gear className={CustomClassNames.SpaceAfterIcon} />
                {`Configuration des répertoires racines`}
            </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <ListGroup>
                {rootDirectories.map((aRootDirectory, index) => <ListGroupItem key={index}>
                    <div className="root-directory">
                        <div className={CssTools.of().then(CustomClassNames.Ellipsis).css()} >{aRootDirectory.rawPath}</div>

                        <ButtonGroup>
                            <BlockingButton label=""
                                toastMessage="Update du rootDirectory"
                                icon={aRootDirectory.hidden ? <EyeSlash /> : <Eye />}
                                variant={aRootDirectory.hidden ? BootstrapVariants.Warning : BootstrapVariants.Success}
                                onClick={() => updateVisibility(aRootDirectory)}
                            />
                            <BlockingButton label=""
                                toastMessage="Update du rootDirectory"
                                icon={aRootDirectory.isProtected ? <Lock /> : <Unlock />}
                                variant={aRootDirectory.isProtected ? BootstrapVariants.Warning : BootstrapVariants.Success}
                                onClick={() => updateProtected(aRootDirectory)}
                            />
                            <BlockingButton label=""
                                toastMessage="Suppression du rootDirectory"
                                icon={<Trash />}
                                variant={BootstrapVariants.Danger}
                                onClick={() => deleteRootDirectory(aRootDirectory)}
                            />
                        </ButtonGroup>
                    </div>
                </ListGroupItem>)}
                <ListGroupItem>
                    <InputGroup>
                        <Form.Control
                            type="text"
                            placeholder="Nouveau répertoire"
                            value={newRootDirectoryPath}
                            onChange={(event) => setNewRootDirectoryPath(event.target.value)}
                        />
                        <BlockingButton
                            label=""
                            icon={<PlusLg />}
                            toastMessage="Ajout du nouveau répertoire en cours"
                            disabled={newRootDirectoryPath === ""}
                            onClick={() => addNewDirectory()}
                        />
                    </InputGroup>
                </ListGroupItem>
            </ListGroup>
        </Modal.Body>
        <Modal.Footer>
            <PassiveBlockingButton variant={BootstrapVariants.Primary} onClick={() => leave()} label="Quitter" />
        </Modal.Footer>
    </Modal>
}

export default RootDirectoriesModal;