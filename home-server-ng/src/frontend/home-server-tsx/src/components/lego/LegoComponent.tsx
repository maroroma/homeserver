import {FC, useEffect, useState} from "react";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import Brick from "../../model/lego/Brick";
import StartWIPAction from "../../context/actions/StartWIPAction";
import LegoRequester from "../../api/LegoRequester";
import EndWIPAction from "../../context/actions/EndWIPAction";
import {Table} from "react-bootstrap";
import CssTools, {CustomClassNames} from "../bootstrap/CssTools";
import {BootstrapText} from "../bootstrap/BootstrapText";
import LegoPicture from "./LegoPicture";
import BrickEditor from "./BrickEditor";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionPlusButton from "../actionmenu/ActionPlusButton";


const LegoComponent: FC = () => {


    const { dispatch, searchString } = useHomeServerContext();

    const [filteredBricks, setFilteredBricks] = useState<Brick[]>([])
    const [rawBricks, setRawBricks] = useState<Brick[]>([])

    const [displayEditionPopup, setDisplayEditionPopup] = useState(false);
    const [displayCreationPopup, setDisplayCreationPopup] = useState(false);

    const [editedBrick, setEditedBrick] = useState(Brick.empty());
    const [newBrick, setNewBrick] = useState(Brick.empty());

    useEffect(() => {

        reloadBricks();


    }, []);


    const reloadBricks = () => {
        dispatch(new StartWIPAction("Chargement des briques"));

        LegoRequester.getAllBricks()
            .then(response => {
                dispatch(new EndWIPAction("Briques chargées"));
                setRawBricks(response);
                setFilteredBricks(response)
            })
    }

    useEffect(() => {
        setFilteredBricks(rawBricks.filter(Brick.filter(searchString)));
    }, [searchString])

    const editBrick = (brick: Brick) => {
        setEditedBrick(brick);
        setDisplayEditionPopup(true);
    }

    const createBrick = () => {
        setNewBrick(Brick.empty())
        setDisplayCreationPopup(true);
    }


    return <div>
        <Table striped>
            <thead>
                <tr className={CssTools.of("table-primary").then(BootstrapText.UpperCase).css()}>
                    <th></th>
                    <th>Nom</th>
                    <th>Tiroir</th>
                </tr>
            </thead>
            {filteredBricks.map((aBrick, index) =>
                <tr key={index}
                    className={CssTools.of("large-row").then(CustomClassNames.Clickable).then(BootstrapText.AlignLeft).css()}
                    onClick={() => { editBrick(aBrick) }}>
                    <td>
                        <LegoPicture brick={aBrick} />
                    </td>
                    <td>
                        {aBrick.name}
                    </td>
                    <td>
                        {aBrick.drawerNumber}
                    </td>
                </tr>
            )}

        </Table>
        <BrickEditor
            createMode={false}
            brick={editedBrick}
            display={displayEditionPopup}
            onHide={(withModification) => {
                setDisplayEditionPopup(false)
                if (withModification) {
                    reloadBricks();
                }
            }}
        />
        <BrickEditor
            createMode={true}
            brick={newBrick}
            display={displayCreationPopup}
            onHide={(withModification) => {
                setDisplayCreationPopup(false)
                if (withModification) {
                    reloadBricks();
                }
            }}
        />
        <ActionMenuComponent alreadyOpen>
            <ActionPlusButton onClick={() => createBrick()}/>
        </ActionMenuComponent>
    </div>
}

export default LegoComponent;