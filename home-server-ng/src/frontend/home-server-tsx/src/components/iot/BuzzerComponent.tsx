import {FC, useEffect, useState} from "react";
import MiniSprite from "../../model/iot/MiniSprite";
import {IotRequester} from "../../api/IotRequester";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";
import EndWIPAction from "../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import {ListGroup, ListGroupItem} from "react-bootstrap";
import SimpleMarginLayout from "../layouts/SimpleMarginLayout";
import MiniSpriteRenderer from "./MiniSpriteRenderer";
import {CustomClassNames} from "../bootstrap/CssTools";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionEditButton from "../actionmenu/ActionEditButton";
import ActionBackButton from "../actionmenu/ActionBackButton";
import {PencilSquare} from "react-bootstrap-icons";
import {useNavigate} from "react-router-dom";
import ActionPlusButton from "../actionmenu/ActionPlusButton";


const BuzzerComponent: FC = () => {

    const { dispatch } = useHomeServerContext();

    const navigate = useNavigate();


    const [allSprites, setAllSprites] = useState<MiniSprite[]>([]);

    const [editMode, setEditMode] = useState(false);

    useEffect(() => {

        dispatch(new StartWIPAction("chargement des sprites"));


        IotRequester.getAllSprites()
            .then(response => {
                dispatch(new EndWIPAction())
                setAllSprites(response);
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement des sprites")));
    }, []);

    const sendBuzz = (selectedSprite: MiniSprite) => {
        dispatch(new StartWIPAction("Demande envoyée"))
        IotRequester.sendBuzz(selectedSprite.name)
            .then(response => dispatch(new EndWIPAction("Buzzer envoyé")))
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de l'émission du buzzer" + error)))
    }


    return <SimpleMarginLayout>
        <ListGroup>
            {allSprites.map((aMiniSprite, index) => <ListGroupItem
                key={index}
                action
                onClick={() => {
                    if (editMode) {
                        navigate(`/buzzer/sprite/${aMiniSprite.name}`)
                    } else {
                        sendBuzz(aMiniSprite)
                    }
                }}
            >
                <div className="sprite-line-container">
                    <div className={CustomClassNames.VerticallyCentered}>
                        {editMode ?
                            <PencilSquare className="h1" />
                            : <MiniSpriteRenderer miniSprite={aMiniSprite} />}
                    </div>
                    <div>
                        <h1>{aMiniSprite.name}</h1>
                        <h3>{aMiniSprite.description}</h3>
                    </div>
                </div>
            </ListGroupItem>)
            }

        </ListGroup>
        <ActionMenuComponent>
            <ActionEditButton
                onClick={() => setEditMode(true)}
                hidden={editMode}
            />
            <ActionPlusButton
                onClick={() => navigate(`/buzzer/sprite/noop`)}
            />
            <ActionBackButton
                onClick={() => setEditMode(false)}
                hidden={!editMode}
            />
        </ActionMenuComponent>
    </SimpleMarginLayout>

}

export default BuzzerComponent;