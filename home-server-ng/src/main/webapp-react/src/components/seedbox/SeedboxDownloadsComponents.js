import React from 'react';
import { useEffect, useState } from 'react';
import seedboxApi from '../../apiManagement/SeedboxApi'
import { administrationApi } from '../../apiManagement/AdministrationApi'
import { useDisplayList } from '../../tools/displayList';
import enhance from '../../tools/enhance';
import { when } from '../../tools/when';
import { ActionMenuComponent, actionMenu } from '../commons/ActionMenuComponent';


import TorrentRendererComponent from './TorrentRendererComponent'

import eventReactor from '../../eventReactor/EventReactor';
import {
    SELECT_ITEM,
    SEARCH_EVENT, FORCE_CLEAR_SEARCH_EVENT
} from '../../eventReactor/EventIds';
import { usePopupDriver, ModalPopupComponent } from '../commons/ModalPopupComponent';

import './SeedboxDownloadsComponents.scss';
import on from '../../tools/on';


export default function SeedboxDownloadsComponents() {


    const [refreshDelay, setRefreshDelay] = useState(5000);
    const [allTorrents, setAllTorrents] = useDisplayList();
    const [addTorrentPopupDriver, setTorrentPopupDriver] = usePopupDriver({
        id: 'addTorrentPopup',
        title: 'Ajouter un nouveau torrent',
        okLabel: 'Ajouter',
        disableOkButton: true
    });

    const [magnetLinksToAdd, setMagnetLinksToAdd] = useState([]);
    const [currentMagnetLinkToAdd, setCurrentMagnetLinkToAdd] = useState("");


    useEffect(() => {

        // premier appel histoire d'init la page
        seedboxApi().getRunningTorrents().then(response => setAllTorrents({
            ...allTorrents
                .update(response)
                .updateItems(enhance().selectable())
        }));

        // recup props pour timer
        administrationApi()
            .getOneProperty("homeserver.seedbox.client.stream.fixedDelay")
            .then(response => setRefreshDelay(parseInt(response.value)));

        const unsubscribeSelectItem = eventReactor().subscribe(SELECT_ITEM, data => {

            if (data.newStatus) {
                actionMenu().open();
            }

            setAllTorrents(
                { ...allTorrents.updateSelectableItems(data.itemId, data.newStatus) })
        });

        const unsubscribePopupClose = eventReactor().shortcuts().onModalClose(driver => {
            setTorrentPopupDriver({...driver});
            setMagnetLinksToAdd([]);
            setCurrentMagnetLinkToAdd("");
        });

        const unsubscribePopupOk = eventReactor().shortcuts().onModalOk(driver => {
            setTorrentPopupDriver({...driver});
            sendAddMagnetRequest();
        })

        return () => {
            unsubscribeSelectItem();
            unsubscribePopupClose();
            unsubscribePopupOk();
        };

    }, []);

    useEffect(() => {
        
        const keyEventHandler = (event) => {
            console.log(event.keyCode)
            if (event.ctrlKey && event.keyCode === 13 && addTorrentPopupDriver.open === false) {
                event.preventDefault();
                openAddTorrentPopup();
            }

            if (event.keyCode === 13 && addTorrentPopupDriver.open === true && currentMagnetLinkToAdd !== "") {
                addCurrentMagnetLinkToList();
            }
        }

        document.addEventListener("keydown", keyEventHandler);

        return () => {
            document.removeEventListener('keydown', keyEventHandler);
        };

    }, [addTorrentPopupDriver, currentMagnetLinkToAdd]);

    const openAddTorrentPopup = () => {
        setTorrentPopupDriver({ ...addTorrentPopupDriver, open: true });
    }

    const addCurrentMagnetLinkToList = () => {
        setMagnetLinksToAdd([...magnetLinksToAdd.concat(currentMagnetLinkToAdd)]);
        setCurrentMagnetLinkToAdd("");
        setTorrentPopupDriver({ ...addTorrentPopupDriver, disableOkButton: false });
    }

    const removeMagnetFromList = (magnetLinkToRemove) => {
        const newList = magnetLinksToAdd.filter(oneLink => oneLink !== magnetLinkToRemove)
        setMagnetLinksToAdd([...newList]);
        setTorrentPopupDriver({ ...addTorrentPopupDriver, disableOkButton: newList.length === 0 });
    }

    const sendAddMagnetRequest = () => {
        seedboxApi().addTorrent(magnetLinksToAdd);
    }


    // une la fréquence de rafraichissement récupérée, on lance l'appel récurrent
    useEffect(() => {
        const intervalToRemove = setInterval(() =>
            seedboxApi().getRunningTorrents()
                .then(response => {
                    const selectionMemento = allTorrents.selectionMemento();
                    setAllTorrents({
                        ...allTorrents
                            .update(response)
                            .updateItems(enhance().selectable())
                            .applySelectionMemento(selectionMemento)
                    })
                })
            , refreshDelay);

        return () => clearInterval(intervalToRemove);

    }, [refreshDelay]);





    return <div>
        <ul className="collection">
            {allTorrents.displayList.map((oneTorrent, torrentIndex) => <TorrentRendererComponent torrent={oneTorrent} key={torrentIndex}></TorrentRendererComponent>)}
        </ul>
        <ActionMenuComponent>
            <li><a href="#!" className={when(allTorrents.hasNoSelectedItems).thenDisableElement("btn-floating btn-small red")}><i className="material-icons">delete</i></a></li>
            <li><a href="#!" className="btn-floating btn-small green" onClick={() => openAddTorrentPopup()}><i className="material-icons">add</i></a></li>
        </ActionMenuComponent>

        <ModalPopupComponent driver={addTorrentPopupDriver}>
            <ul className="collection">
                {magnetLinksToAdd.map(oneMagnetLink => <li className="collection-item">
                    <div>
                        {oneMagnetLink}
                        <a href="#!" className="secondary-content" onClick={() => removeMagnetFromList(oneMagnetLink)}>
                            <i className="material-icons red-font">delete</i>
                        </a>
                    </div>
                </li>)}
                <li className="collection-item">
                    <div className="row">
                        <div className="col s9">
                            <input type="text" className="validate"
                                value={currentMagnetLinkToAdd}
                                placeholder="Saisir un magnetlink"
                                onChange={(event) => setCurrentMagnetLinkToAdd(event.target.value)} />
                        </div>
                        <div className="col s3 right">
                            <a href="#!" className={when(currentMagnetLinkToAdd === "").thenDisableElement("btn prefix")}
                                onClick={() => addCurrentMagnetLinkToList()}>
                                <i className="material-icons">add_circle_outline</i>
                            </a>
                        </div>
                    </div>
                </li>
            </ul>
        </ModalPopupComponent>
    </div>
}