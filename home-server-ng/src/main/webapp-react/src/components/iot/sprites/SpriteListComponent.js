import React from 'react';
import { useEffect, useState } from 'react';
import { useDisplayList, DisplayList } from '../../../tools/displayList';
import iotApi from '../../../apiManagement/IotApi';
import SpriteRendererComponent from './SpriteRendererComponent';
import { usePopupDriver, ModalPopupComponent } from '../../commons/ModalPopupComponent';
import { ActionMenuComponent, actionMenu } from '../../commons/ActionMenuComponent';
import iotSubEventReactor from '../iotSubEventReactor';
import SpriteEditorComponent from './SpriteEditorComponent';
import sort from '../../../tools/sort';
import eventReactor from '../../../eventReactor/EventReactor';
import { SELECT_ITEM } from '../../../eventReactor/EventIds';
import { when } from '../../../tools/when';


export default function SpriteListComponent({ onlyDisplay = false }) {


    const [spriteList, setSpriteList] = useDisplayList();
    const [spriteEditorPopupDriver, setSpriteEditorPopupDriver] = usePopupDriver({
        id: "spriteEditorPopup",
        title: "Editer un sprite"
    });
    const [isEditMode, setEditMode] = useState(true);


    const [spriteToEdit, setSpriteToEdit] = useState({});

    const createEmptySprite = () => {
        const lines = [];
        for (let lineIndex = 0; lineIndex < 8; lineIndex++) {
            const oneLine = [];
            for (let rowIndex = 0; rowIndex < 8; rowIndex++) {
                oneLine.push({
                    on: false
                });
            }

            lines.push(oneLine);
        }

        return {
            name: "",
            description: "",
            lines: lines
        }
    }

    useEffect(() => {
        iotApi().getAllSprites()
            .then(response => setSpriteList({
                ...spriteList.update(response).updateSort(sort().basic(oneSprite => oneSprite.name))
            }));

        const editSpriteUnsubscribe = iotSubEventReactor().onEditSprite(spriteToEdit => {

            setEditMode(true);

            setSpriteEditorPopupDriver({
                ...spriteEditorPopupDriver,
                open: true,
                title: `Editer le sprite ${spriteToEdit.name}`
            });

            setSpriteToEdit({ ...spriteToEdit });
        });

        const saveSpriteUnsubscrive = iotSubEventReactor().onSaveSprite(spriteToSave => {
            iotApi().updateSprite(spriteToSave)
                .then(response => setSpriteList({
                    ...spriteList.update(response)
                }));
        });

        const createSpriteUnsubcribe = iotSubEventReactor().onCreateSprite(spriteToCreate => {
            iotApi().createSprite(spriteToCreate)
                .then(response => setSpriteList({
                    ...spriteList.update(response)
                }));
        });

        const unsubscribeSelectItem = eventReactor().subscribe(SELECT_ITEM, data => {

            if (data.newStatus) {
                actionMenu().open();
            }

            setSpriteList(
                { ...spriteList.updateSelectableItems(data.itemId, data.newStatus, oneSprite => oneSprite.name) })
        });


        return () => {
            editSpriteUnsubscribe();
            saveSpriteUnsubscrive();
            unsubscribeSelectItem();
            createSpriteUnsubcribe();
        }


    }, []);

    const sendDeleteRequest = () => {
        iotApi().deleteSprites(spriteList.getSelectedItems())
            .then(() => iotApi().getAllSprites())
            .then(response => setSpriteList({
                ...spriteList.update(response)
            }));
    };

    const openAddNewSprite = () => {
        setEditMode(false);
        setSpriteToEdit(createEmptySprite());
        setSpriteEditorPopupDriver({
            ...spriteEditorPopupDriver,
            open: true,
            title: "Créer un nouveau sprite"
        });
    }
    const actionMenuComponent = onlyDisplay ? null : <ActionMenuComponent>
        <li>
            <a href="#!" className={when(spriteList.hasNoSelectedItems).thenDisableElement("btn-floating btn-small red")}
                onClick={() => sendDeleteRequest()}>
                <i className="material-icons">delete</i>
            </a>
        </li>
        <li>
            <a href="#!" className="btn-floating btn-small green" onClick={() => openAddNewSprite()}>
                <i className="material-icons">add</i>
            </a>
        </li>
    </ActionMenuComponent>;


    return <div>
        <ul className="collection">
            {spriteList.displayList.map((oneSprite, index) =>
                <SpriteRendererComponent sprite={oneSprite} editMode={!onlyDisplay} key={index}></SpriteRendererComponent>
            )}
        </ul>

        <ModalPopupComponent driver={spriteEditorPopupDriver}>
            <SpriteEditorComponent spriteToEdit={spriteToEdit} popupDriver={spriteEditorPopupDriver} newSprite={!isEditMode}></SpriteEditorComponent>
        </ModalPopupComponent>

        {actionMenuComponent}


    </div>
}