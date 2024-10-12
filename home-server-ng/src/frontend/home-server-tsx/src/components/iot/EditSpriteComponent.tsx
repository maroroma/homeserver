import {FC, useEffect, useState} from "react";
import StartWIPAction from "../../context/actions/StartWIPAction";
import {IotRequester} from "../../api/IotRequester";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import EndWIPAction from "../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import {useNavigate, useParams} from "react-router-dom";
import MiniSprite from "../../model/iot/MiniSprite";
import SimpleMarginLayout from "../layouts/SimpleMarginLayout";
import CssTools from "../bootstrap/CssTools";
import {BootstrapText} from "../bootstrap/BootstrapText";
import {Form} from "react-bootstrap";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionBackButton from "../actionmenu/ActionBackButton";
import ActionSaveButton from "../actionmenu/ActionSaveButton";
import ActionDeleteButton from "../actionmenu/ActionDeleteButton";
import YesNoModal from "../modals/YesNoModal";


const EditSpriteComponent: FC = () => {

    const { dispatch } = useHomeServerContext();

    const { spriteId } = useParams();

    const [spriteToEdit, setSpriteToEdit] = useState(MiniSprite.empty())

    const [displayDeletePopup, setDisplayDeletePopup] = useState(false);

    const [creationMode, setCreationMode] = useState(false);

    const [isValid, setIsValid] = useState(true);

    const navigate = useNavigate();


    useEffect(() => {

        dispatch(new StartWIPAction("chargement des sprites"));

        IotRequester.getAllSprites()
            .then(response => {
                dispatch(new EndWIPAction())
                const spriteToEdit = response.find(aSprite => aSprite.name === spriteId);
                if (spriteToEdit) {
                    setSpriteToEdit(spriteToEdit);
                    setCreationMode(false);
                } else {
                    setSpriteToEdit(MiniSprite.empty());
                    setIsValid(false);
                    setCreationMode(true);
                }
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement des sprites")));
    }, [spriteId]);

    const updatePixel = (line: number, column: number) => {
        spriteToEdit.lines[line][column].on = !spriteToEdit.lines[line][column].on
        setSpriteToEdit({
            ...spriteToEdit,
            lines: [...spriteToEdit.lines]
        })
    }

    const updateDescription = (description: string) => {
        setSpriteToEdit(
            {
                ...spriteToEdit,
                description: description
            }
        )
    }
    const updateId = (id: string) => {

        setIsValid(id !== undefined && id !== "");

        setSpriteToEdit(
            {
                ...spriteToEdit,
                name: id
            }
        )
    }

    const saveSprite = () => {
        if (!creationMode) {
            IotRequester.updateSprite(spriteToEdit)
                .then(response => {
                    dispatch(new EndWIPAction("Sprite sauvegardé"))
                    navigate("/buzzer");
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la sauvegarde du sprite")))
        } else {
            IotRequester.createSprite(spriteToEdit)
                .then(response => {
                    dispatch(new EndWIPAction("Sprite sauvegardé"))
                    navigate("/buzzer");
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la sauvegarde du sprite")))
        }
    }

    const deleteSprite = () => {
        IotRequester.deleteSprite(spriteToEdit)
            .then(response => {
                dispatch(new EndWIPAction("Suppression terminée"));
                navigate("/buzzer");
                setDisplayDeletePopup(false);
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la suppression du sprite")))

    }

    return <SimpleMarginLayout>
        <div className={BootstrapText.AlignCenter}>
            <table className="edit-renderer">
                {spriteToEdit.lines.map((aline, lineIndex) => {
                    return <tr key={lineIndex}>
                        {aline.map((aColumn, columnIndex) => {
                            return <td
                                className={
                                    CssTools
                                        .of("pixel")
                                        .if(aColumn.on, "pixel-on")
                                        .css()}
                                key={`${lineIndex}-${columnIndex}`}
                                onClick={() => updatePixel(lineIndex, columnIndex)}
                            >

                            </td>
                        })}
                    </tr>
                })}
            </table>
        </div>
        <SimpleMarginLayout>
            <div data-bs-theme="light">
                <Form.Group className="mb-3">
                    <Form.Label>Identifiant du sprite</Form.Label>
                    <Form.Control
                        disabled={!creationMode}
                        type="text"
                        placeholder="Identifiant du sprite"
                        value={spriteToEdit.name}
                        onChange={(event) => updateId(event.target.value)}
                        isInvalid={!isValid}
                    />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Description du sprite</Form.Label>
                    <Form.Control type="text" placeholder="Descriptiopn du sprite"
                        value={spriteToEdit.description}
                        onChange={(event) => updateDescription(event.target.value)}
                    />
                </Form.Group>
            </div>
        </SimpleMarginLayout>

        <ActionMenuComponent alreadyOpen>
            <ActionBackButton onClick={() => navigate("/buzzer")} />
            <ActionSaveButton
                toastMessage="Sauvegarde du sprite en cours"
                blockingButton
                onClick={() => saveSprite()}
                disabled={!isValid}
            />
            <ActionDeleteButton
                hidden={creationMode}
                onClick={() => setDisplayDeletePopup(true)}
            />
        </ActionMenuComponent>

        <YesNoModal
            displayYesNoPopup={displayDeletePopup}
            title={`Supprimer un sprite`}
            question={`Voulez vous vraiment supprimer le sprite d'id ${spriteToEdit.name} ?`}
            noLabel="Annuler"
            yesLabel="Supprimer"
            onYesClick={() => { deleteSprite() }}
            onNoClick={() => { setDisplayDeletePopup(false) }}
        />
    </SimpleMarginLayout>
}

export default EditSpriteComponent;