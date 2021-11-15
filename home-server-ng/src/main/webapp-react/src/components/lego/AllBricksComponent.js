import React, { useEffect, useRef, useState } from 'react';
import eventReactor from '../../eventReactor/EventReactor';
import { useDisplayList } from '../../tools/displayList';
import DataGridComponent from '../commons/DataGridComponent';
import legoApi from '../../apiManagement/LegoApi';
import { ModalPopupComponent, usePopupDriver } from '../commons/ModalPopupComponent';

import './AllBricksComponent.scss';
import { searchSubReactor } from '../mainmenu/SearchBarComponent';
import on from '../../tools/on';
import IconComponent from '../commons/IconComponent';

export default function AllBricksComponent() {


    const [allBricks, setAllBricks] = useDisplayList();

    const ADD_BRICK_POPUP_ID = 'addBrickPopup';


    const [brickToAdd, setBrickToAdd] = useState({
        name: "",
        drawerNumber: 0,
        pictureUrl: ""
    });

    const [addBrickPopupDriver, setBrickPopupDriver] = usePopupDriver({
        id: ADD_BRICK_POPUP_ID,
        title: 'Ajouter une nouvelle brique',
        okLabel: 'Ajouter',
        disableOkButton: true
    });

    // pour claquer le focus
    const brickNameInput = useRef(null);

    useEffect(() => {


        // sauvegarde nouvelle brique
        const unsubscribePopupOk = eventReactor().shortcuts().onModalOkFor(ADD_BRICK_POPUP_ID, () =>
            legoApi().addBrick(brickToAdd).then(response => setAllBricks(
                { ...allBricks.update(response) }))
        );

        // gestion de l'état d'activation du bouton en fonction de la saisie
        setBrickPopupDriver({
            ...addBrickPopupDriver,
            disableOkButton: validateNewBrick(brickToAdd)
        });


        return () => {
            unsubscribePopupOk();
        }


    }, [brickToAdd]);

    // abos events par défaut + chargement initial de la grille
    useEffect(() => {

        // chargement initial de la grille
        legoApi().getAllBricks()
            .then(response => setAllBricks(
                { ...allBricks.update(response) }));

        // pour démarrer la saisie directement
        const unsubscribeModalOpened = eventReactor().shortcuts().onModalOpenedFor(ADD_BRICK_POPUP_ID, () => brickNameInput.current.focus());

        // ouverture de la popup d'ajout
        const unsubscribeDataGridAddItem = eventReactor().shortcuts().onDataGridAddItem(() => {
            openAddBrickPopup();
        });

        // fermeture de la popup d'ajout, on clean la saisie
        const unsubscribePopupClose = eventReactor().shortcuts().onModalCloseFor(ADD_BRICK_POPUP_ID, () => setBrickToAdd({
            name: "",
            drawerNumber: 0,
            pictureUrl: ""
        }));

        // sauvegarde des modifications
        const unsubscribeDataGridSaveItems = eventReactor().shortcuts().onDataGridSaveItems(itemsToSave => {
            legoApi().updateBricks(itemsToSave).then(response => setAllBricks(
                { ...allBricks.update(response) }))
        });

        // suppression d'une brique
        const unsubscribeDataGridDeleteOneItem = eventReactor().shortcuts().onDataGridDeleteOne(oneItemToDelete => {
            legoApi().deleteBrick(oneItemToDelete)
                .then(response => setAllBricks(
                    { ...allBricks.update(response).clearFilters() }));
        });

        // gestion de la recherche d'une brique
        const unsubscribeSearchEvent = searchSubReactor().onSearchEvent(data => {

            if (data === "") {
                setAllBricks({
                    ...allBricks
                        .clearFilters()
                });
            } else {


                // séparation de tous les mots
                const allRawStringFilters = data
                    .split(" ")
                    .filter(on().notNull())
                    .filter(on().stringIsNotEmpty());

                // détection des chiffres, potentiellement les 4x2 etc, pour inverser la gestion des dimensions
                const regexNumber = /[0-9]/g;
                const allDimentionsFilters = [].concat.apply([],
                    allRawStringFilters
                        .map(oneDataField => oneDataField.match(regexNumber))
                        .filter(on().notNull()));

                // concat champ standard et chiffres
                const allStringFilters = allRawStringFilters
                    .filter(oneItem => !on().stringMatches(regexNumber)(oneItem))
                    .concat(allDimentionsFilters);



                setAllBricks({
                    ...allBricks
                        .updateFilter(
                            on()
                                .stringContainsAllOf(allStringFilters, oneItem => oneItem.name))
                });
            }
        }
        );



        return () => {
            unsubscribeDataGridAddItem();
            unsubscribePopupClose();
            unsubscribeModalOpened();
            unsubscribeDataGridSaveItems();
            unsubscribeDataGridDeleteOneItem();
            unsubscribeSearchEvent();
        }


    }, []);

    const openAddBrickPopup = () => {
        setBrickPopupDriver({ ...addBrickPopupDriver, open: true });
    };

    const validateNewBrick = newBrick => newBrick.name === "" || typeof newBrick.drawerNumber !== 'number' || newBrick.drawerNumber <= 0;


    // gestion events clavier
    useEffect(() => {

        const keyEventHandler = (event) => {
            if (event.ctrlKey && event.keyCode === 13 && addBrickPopupDriver.open === false) {
                event.preventDefault();
                openAddBrickPopup();
            }

            if (event.keyCode === 13 && addBrickPopupDriver.open === true) {
                console.log("coucou", validateNewBrick(brickToAdd));
                event.preventDefault();
                if (!validateNewBrick(brickToAdd)) {
                    console.log("coucou2");
                    setBrickPopupDriver({ ...addBrickPopupDriver, open: false });
                    legoApi().addBrick(brickToAdd).then(response => setAllBricks(
                        { ...allBricks.update(response) }));
                }

            }
        }

        document.addEventListener("keydown", keyEventHandler);

        return () => {
            document.removeEventListener('keydown', keyEventHandler);
        };

    }, [addBrickPopupDriver, brickToAdd]);

    const customBrickPictureRenderer = (row, type, editCallBack) => {
        if (row.pictureFileId !== undefined && row.pictureFileId !== null) {
            return <img className="brick" src={legoApi().downloadBaseUrl() + "/" + row.id + "/picture"}></img>
        } else {
            return <IconComponent icon="wallpaper" onClick={editCallBack}></IconComponent>
        }
    }


    const dataGridConfiguration = {
        itemUniqueId: 'id',
        displaySaveButton: true,
        displayAddButton: true,
        columns: [
            {
                dataField: 'pictureUrl',
                renderer: 'custom',
                customRenderer: customBrickPictureRenderer,
                customClass: 'brick',
                onClickEditField: 'pictureUrl',
            }, {
                header: 'Nom',
                dataField: 'name',
                defaultSort: true,
                onClickEditField: 'name',
            },
            {
                header: 'Tiroir',
                dataField: 'drawerNumber',
                onClickEditField: 'drawerNumber',
            },
            {
                isDeleteButton: true
            }
        ]
    };


    return <div>
        <DataGridComponent configuration={dataGridConfiguration} data={allBricks.displayList}></DataGridComponent>
        <ModalPopupComponent driver={addBrickPopupDriver}>
            <div className="row">
                <div className="col s12">
                    <input type="text" className="validate"
                        value={brickToAdd.name}
                        placeholder="Nom de la brique"
                        ref={brickNameInput}
                        onChange={(event) => setBrickToAdd({
                            ...brickToAdd,
                            name: event.target.value
                        })} />
                </div>
            </div>
            <div className="row">
                <div className="col s12">
                    <input type="number" className="validate"
                        value={brickToAdd.drawerNumber}
                        placeholder="Tiroir"
                        onChange={(event) => setBrickToAdd({
                            ...brickToAdd,
                            drawerNumber: event.target.valueAsNumber
                        })} />
                </div>
            </div>

            <div className="row">
                <div className="col s12">
                    <input type="text" className="validate"
                        value={brickToAdd.pictureUrl}
                        placeholder="Url vers l'image de la brique"
                        onChange={(event) => setBrickToAdd({
                            ...brickToAdd,
                            pictureUrl: event.target.value
                        })} />
                </div>
            </div>

        </ModalPopupComponent>
    </div>;
}