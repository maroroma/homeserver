import {FC, useEffect, useState} from "react";
import RunningTorrent from "../../model/seedbox/RunningTorrent";
import SeedboxRequester from "../../api/SeedboxRequester";
import {Button, Form, InputGroup, ListGroup, ListGroupItem, ProgressBar} from "react-bootstrap";
import CssTools, {CustomClassNames} from "../bootstrap/CssTools";
import {PlusCircle, Trash} from "react-bootstrap-icons";
import SimpleMarginLayout from "../layouts/SimpleMarginLayout";
import Formatters from "../Formatters";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import {BootstrapText} from "../bootstrap/BootstrapText";
import BlockingButton from "../blockingbutton/BlockingButton";
import NewTorrents from "../../model/seedbox/NewTorrents";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import EndWIPAction from "../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import YesNoModal from "../modals/YesNoModal";
import StartWIPAction from "../../context/actions/StartWIPAction";
import ToastAction from "../../context/actions/ToastAction";


const SeedBoxComponent: FC = () => {

    const { dispatch } = useHomeServerContext();

    const [runningTorrents, setRunningTorrents] = useState<RunningTorrent[]>([]);

    const [displayDeletePopup, setDisplayDeletePopup] = useState(false);
    const [selectedTorrentToDelete, setSelectedTorrentToDelete] = useState<RunningTorrent>();

    const [magnetLinks, setMagnetLinks] = useState(NewTorrents.empty());



    useEffect(() => {

        const intervalToRemove = setInterval(() => SeedboxRequester.getRunningTorrents()
            .then(response => setRunningTorrents(response)), 10000);

        SeedboxRequester.getRunningTorrents()
            .then(response => setRunningTorrents(response))
            .catch(error => dispatch(ToastAction.error("Erreur rencontrée lors de la récupération des torrents")))

        return () => clearInterval(intervalToRemove);
    }, []);

    const updateMagnetLinkToAdd = (magnetLink: string) => {
        setMagnetLinks({
            ...magnetLinks,
            magnetLinks: [magnetLink]
        })
    }

    const requestToAddNewTorrent = () => {

        SeedboxRequester.addTorrent(magnetLinks)
            .then(response => {
                dispatch(new EndWIPAction("Demande d'ajout de torrent émise"))
                setMagnetLinks(NewTorrents.empty());
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la demande d'ajout de torrent")))

    }

    const deleteTorrent = () => {
        if (selectedTorrentToDelete) {
            dispatch(new StartWIPAction("Emission de la demande de suppression"));
            SeedboxRequester.deleteTorrent(selectedTorrentToDelete)
                .then(response => dispatch(new EndWIPAction("Demande de suppression émise")))
                .catch(response => dispatch(new EndWIPInErrorAction("Demande de suppression en erreur")));
        }
    }


    return <SimpleMarginLayout>
        <ListGroup data-bs-theme="light">
            {runningTorrents.map(aTorrent => {
                return <ListGroup.Item className={BootstrapText.AlignLeft}>
                    <h2>
                        {aTorrent.name}
                        <Button
                            variant={BootstrapVariants.Danger}
                            className={CustomClassNames.PullRight}
                            onClick={() => {
                                setSelectedTorrentToDelete(aTorrent);
                                setDisplayDeletePopup(true);
                            }}
                        >
                            <Trash />
                        </Button>
                    </h2>
                    <ProgressBar
                        now={aTorrent.percentDone}
                        label={`${Formatters.octets(aTorrent.done)} / ${Formatters.octets(aTorrent.total)}`}
                        variant={CssTools.of().ifElse(aTorrent.completed, BootstrapVariants.Success, BootstrapVariants.Info).css()}
                    />

                </ListGroup.Item>
            })
            }
            <ListGroupItem>
                <InputGroup className="mb-3">
                    <Form.Control
                        placeholder="Magnet Link"
                        value={magnetLinks.magnetLinks[0]}
                        onChange={(event) => updateMagnetLinkToAdd(event.target.value)}
                    />
                    <BlockingButton
                        icon={<PlusCircle />}
                        toastMessage="Ajout du torrent en cours"
                        label=""
                        onClick={() => requestToAddNewTorrent()}
                        disabled={magnetLinks.magnetLinks.length === 0 || magnetLinks.magnetLinks[0] === ""}
                    />
                </InputGroup>
            </ListGroupItem>
        </ListGroup>
        <YesNoModal
            displayYesNoPopup={displayDeletePopup}
            onNoClick={() => setDisplayDeletePopup(false)}
            onYesClick={() => {
                deleteTorrent()
                setDisplayDeletePopup(false);
            }}
            question={`Voulez vous vraiment supprimer le torrent ${selectedTorrentToDelete?.name} ?`}
            title="Supprimer un torrent ?"
            yesLabel="Supprimer"
            noLabel="Annuler"
        />
    </SimpleMarginLayout>
}

export default SeedBoxComponent;