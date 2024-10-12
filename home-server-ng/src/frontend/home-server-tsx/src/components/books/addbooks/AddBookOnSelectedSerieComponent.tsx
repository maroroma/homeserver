import {FC, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import StartWIPAction from "../../../context/actions/StartWIPAction";
import BooksRequester from "../../../api/BooksRequester";
import SelectedSerieLoadedAction from "../../../context/actions/books/SelectedSerieLoadedAction";
import ToastAction from "../../../context/actions/ToastAction";
import {Button, Card, CardBody, Col, Form, InputGroup, Row} from "react-bootstrap";
import SerieCardHeader from "../SerieCardHeader";
import {BookmarkDash, BookmarkPlus, BoxArrowUpRight, Search} from "react-bootstrap-icons";
import BlockingButton from "../../blockingbutton/BlockingButton";
import ImportBookProposal from "../../../model/books/ImportBookProposal";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import EndWIPAction from "../../../context/actions/EndWIPAction";
import SelectableItems from "../../../model/SelectableItems";
import {BootstrapVariants} from "../../bootstrap/BootstrapVariants";
import ActionMenuComponent from "../../actionmenu/ActionMenuComponent";
import ActionBookPlusButton from "../../actionmenu/ActionBookPlusButton";
import ActionBookMinusButton from "../../actionmenu/ActionBookMinusButton";
import ActionCheckButton from "../../actionmenu/ActionCheckButton";
import {CustomClassNames} from "../../bootstrap/CssTools";
import SimpleMarginLayout from "../../layouts/SimpleMarginLayout";


const AddBookOnSelectedSerieComponent: FC = () => {
    const firstNames = ["Rodolphe", "Tom", "Liam", "Simon", "Marie"];

    const { serieId } = useParams();

    const { dispatch, allBooksSubState, workInProgress } = useHomeServerContext();
    const navigate = useNavigate();


    const [bookProposals, setBookProposals] = useState<SelectableItems<ImportBookProposal>>(SelectableItems.empty());

    const [sanctuaryUrl, setSanctuaryUrl] = useState("");
    const [prefix, setPrefix] = useState("Tome");
    const [selectedOwner, setSelectedOwner] = useState(firstNames[0]);


    useEffect(() => {
        if (serieId) {
            dispatch(new StartWIPAction("Chargement de la série en cours"));
            BooksRequester.getSerie(serieId)
                .then(response => dispatch(new SelectedSerieLoadedAction(response)))
                .catch(error => dispatch(ToastAction.error("Erreur rencontrée lors du chargement de la série")))
        }
    }, [serieId]);

    useEffect(() => {
        setSanctuaryUrl(allBooksSubState.selectedSerie.serie.serieUrlForImport)
    }, [allBooksSubState.selectedSerie.serie]);

    const searchForBookProposals = () => {
        BooksRequester.getBooksProposalForSerie(allBooksSubState.selectedSerie.serie, sanctuaryUrl)
            .then(response => {
                setBookProposals(SelectableItems.of(response.filter(aProposal => aProposal.alreadyInCollection === false)));
                dispatch(new EndWIPAction());
            })
            .catch(error => dispatch(new EndWIPInErrorAction("erreur rencontrée lors de la récupération des livres à ajouter")))
    }

    const addProposalsToSelectedSerie = () => {
        BooksRequester.addBooksToSerieFromProposals(
            allBooksSubState.selectedSerie.serie,
            prefix,
            selectedOwner,
            bookProposals.selectedItems()
        )
            .then(response => {
                navigate(`/books/serieDetails/${allBooksSubState.selectedSerie.serie.id}`)
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Une erreur est survenue lors de l'ajout des nouveaux tomes")))
    }

    return <SimpleMarginLayout>
        <Card className="full-serie">
            <SerieCardHeader
                serieToDisplay={allBooksSubState.selectedSerie.serie}
                onClick={() => {
                    navigate(`/books/serieDetails/${allBooksSubState.selectedSerie.serie.id}`)
                }} />
            <Card.Body>
                <InputGroup className="mb-3">
                    <Button href="https://www.sanctuary.fr" target="_blank">
                        <BoxArrowUpRight className={CustomClassNames.SpaceAfterIcon} />
                        Url Sanctuary
                    </Button>
                    <Form.Control
                        disabled={workInProgress}
                        isInvalid={sanctuaryUrl === undefined || sanctuaryUrl === ""}
                        id="basic-url"
                        aria-describedby="basic-addon3"
                        value={sanctuaryUrl}
                        onChange={(event) => setSanctuaryUrl(event.target.value)}
                    />
                    <BlockingButton
                        icon={<Search />}
                        disabled={sanctuaryUrl === undefined || sanctuaryUrl === ""}
                        label=""
                        onClick={() => searchForBookProposals()}
                        toastMessage="Recherche des livres manquants"
                    />
                </InputGroup>

            </Card.Body>
        </Card>
        <Card>
            <Card.Header as="h1">{bookProposals.items.length} Tome(s) à Ajouter ({bookProposals.nbSelected()} sélectionnés)</Card.Header>
            <CardBody>
                <InputGroup className="mb-3">
                    <InputGroup.Text id="basic-addon3" >
                        Préfixe
                    </InputGroup.Text>
                    <Form.Control
                        id="basic-url"
                        aria-describedby="basic-addon3"
                        value={prefix}
                        onChange={(event) => setPrefix(event.target.value)}
                    />
                </InputGroup>
                <InputGroup className="mb-3">
                    <InputGroup.Text>
                        Proprio
                    </InputGroup.Text>
                    <Form.Select
                        onChange={(event) => setSelectedOwner(event.currentTarget.value)}
                        value={selectedOwner}>
                        {firstNames.map((aFirstName, index) => <option value={aFirstName}>{aFirstName}</option>)}
                    </Form.Select>
                </InputGroup>
                <Row xs={2} md={2} lg={6}>
                    {bookProposals.items.map((aBookProposal, idx) => (
                        <Col key={idx}>
                            <Card
                                className={CustomClassNames.LayoutWithMargin}
                                bg={aBookProposal.map(BootstrapVariants.Primary, BootstrapVariants.Dark)}
                                onClick={() => setBookProposals(bookProposals.switch(aBookProposal, bookProposal => bookProposal.number))}

                            >
                                <Card.Img variant="top" src={aBookProposal.item.initialImageLink} className="serie-picture" />
                                <Card.Footer>
                                    <Button
                                        variant={aBookProposal.map(BootstrapVariants.Danger, BootstrapVariants.Primary)}
                                        className={CustomClassNames.FullWidth}
                                    >
                                        {aBookProposal.map(<BookmarkDash />, <BookmarkPlus />)} {prefix} {aBookProposal.item.number}
                                    </Button>
                                </Card.Footer>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </CardBody>
        </Card>
        <ActionMenuComponent alreadyOpen={true}>
            <ActionBookPlusButton disabled={bookProposals.allSelected()} onClick={() => setBookProposals(bookProposals.selectAll())} />
            <ActionBookMinusButton disabled={bookProposals.noneSelected()} onClick={() => setBookProposals(bookProposals.unselectAll())} />
            <ActionCheckButton
                disabled={bookProposals.noneSelected()}
                toastMessage="Ajout des nouveaux tomes à la série"
                blockingButton={true}
                onClick={() => addProposalsToSelectedSerie()}
            />

        </ActionMenuComponent>
    </SimpleMarginLayout>

}

export default AddBookOnSelectedSerieComponent;