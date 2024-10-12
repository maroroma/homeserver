import {FC, useEffect, useState} from "react"
import {Form, Image, InputGroup, Modal} from "react-bootstrap";
import Brick from "../../model/lego/Brick";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {BootstrapText} from "../bootstrap/BootstrapText";
import LegoPicture from "./LegoPicture";
import PassiveBlockingButton from "../blockingbutton/PassiveBlockingButton";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import BlockingButton from "../blockingbutton/BlockingButton";
import LegoRequester from "../../api/LegoRequester";
import EndWIPAction from "../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";


export type BrickEditorProps = {
    brick: Brick,
    display: boolean,
    onHide: (withModification: boolean) => void,
    createMode: boolean
}

const BrickEditor: FC<BrickEditorProps> = ({ brick, display, onHide, createMode }) => {

    const { workInProgress, dispatch } = useHomeServerContext();

    const [editedBrick, setEditedBrick] = useState(Brick.empty());

    const [nameIsValid, setNameIsValid] = useState(true);
    const [drawerIsValid, setDrawerIsValid] = useState(true);
    const [urlIsValid, setUrlIsValid] = useState(true);


    useEffect(() => {
        setNameIsValid(editedBrick.name !== "");
        setDrawerIsValid(editedBrick.drawerNumber > 0);
        setUrlIsValid(editedBrick.pictureUrl !== "");

    }, [editedBrick])


    const updateLabel = (newLabel: string) => {
        setEditedBrick({
            ...editedBrick,
            name: newLabel
        })


    }

    const updateDrawer = (newDrawer: number) => {
        setEditedBrick({
            ...editedBrick,
            drawerNumber: newDrawer
        })
    }

    const updateUrl = (newUrl: string) => {
        setEditedBrick({
            ...editedBrick,
            pictureUrl: newUrl
        })
    }

    useEffect(() => {
        setEditedBrick({
            ...brick
        })

    }, [brick])


    const updateOrSave = () => {
        if (createMode) {
            LegoRequester.createBrick(editedBrick)
                .then(response => {
                    dispatch(new EndWIPAction("Brique créée"));
                    onHide(true);
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la création de la brique")))

        } else {
            LegoRequester.updateBrick(editedBrick)
                .then(response => {
                    dispatch(new EndWIPAction("Brique sauvegardée"));
                    onHide(true);
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la sauvegarde de la brique")))
        }
    }

    const deleteBrick = () => {
        LegoRequester.deleteBrick(editedBrick)
            .then(response => {
                dispatch(new EndWIPAction("Brique Supprimée"));
                onHide(true);
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la suppression de la brique")))
    }









    return <Modal show={display} fullscreen={true} onHide={() => onHide(false)}>
        <Modal.Header closeButton={!workInProgress}>
            <Modal.Title className="text-break-anywhere">Edition de la brique <strong>{brick.name}</strong></Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <div className={BootstrapText.AlignCenter}>
                <LegoPicture brick={editedBrick} />
            </div>
            <InputGroup className="mb-3">
                <InputGroup.Text >Nom</InputGroup.Text>
                <Form.Control
                    placeholder="Nom de la brique"
                    value={editedBrick.name}
                    onChange={(event) => updateLabel(event.target.value)}
                    isInvalid={!nameIsValid}
                />
            </InputGroup>
            <InputGroup className="mb-3">
                <InputGroup.Text >Tiroir</InputGroup.Text>
                <Form.Control
                    placeholder="Tiroir"
                    value={editedBrick.drawerNumber}
                    onChange={(event) => updateDrawer(Number(event.target.value))}
                    isInvalid={!drawerIsValid}
                />
            </InputGroup>

            <InputGroup className="mb-3">
                <InputGroup.Text >Picture url</InputGroup.Text>
                <Form.Control
                    placeholder="Picture url"
                    value={editedBrick.pictureUrl}
                    isInvalid={!urlIsValid}
                    onChange={(event) => updateUrl(event.target.value)}
                />
            </InputGroup>
            <div className={BootstrapText.AlignCenter}>
                <Image src={editedBrick.pictureUrl} className="lego-picture"></Image>
            </div>
        </Modal.Body>
        <Modal.Footer>
            <PassiveBlockingButton
                variant={BootstrapVariants.Secondary}
                onClick={() => onHide(false)}
                label="Annuler"
            />
            {createMode ? <></> :
                <BlockingButton
                    label="Supprimer"
                    variant={BootstrapVariants.Danger}
                    toastMessage="Suppression de la brique en cours"
                    onClick={() => { deleteBrick() }}
                />
            }
            <BlockingButton
                label={createMode ? "Ajouter" : "Sauvegarder"}
                variant={BootstrapVariants.Primary}
                toastMessage="Enregistrement de la brique en cours"
                onClick={() => { updateOrSave() }}
                disabled={!nameIsValid || !urlIsValid || !drawerIsValid}
            />
        </Modal.Footer>
    </Modal>
}


export default BrickEditor;