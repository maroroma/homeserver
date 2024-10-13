import {FC, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {Card, CardText, Col, Image, Modal, Row} from "react-bootstrap";
import {BootstrapText} from "../bootstrap/BootstrapText";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionDeleteButton from "../actionmenu/ActionDeleteButton";
import Book from "../../model/books/Book";
import BooksRequester from "../../api/BooksRequester";
import StartWIPAction from "../../context/actions/StartWIPAction";
import ActionBackButton from "../actionmenu/ActionBackButton";
import SelectedSerieLoadedAction from "../../context/actions/books/SelectedSerieLoadedAction";
import SelectedSerieBooksLoadedAction from "../../context/actions/books/SelectedSerieBooksLoadedAction";
import ActionBookPlusButton from "../actionmenu/ActionBookPlusButton";
import SerieCardHeader from "./SerieCardHeader";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import SimpleMarginLayout from "../layouts/SimpleMarginLayout";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import PassiveBlockingButton from "../blockingbutton/PassiveBlockingButton";
import BlockingButton from "../blockingbutton/BlockingButton";
import CssTools, {CustomClassNames} from "../bootstrap/CssTools";
import {PersonCircle, Trash} from "react-bootstrap-icons";
import EndWIPAction from "../../context/actions/EndWIPAction";
import YesNoModal from "../modals/YesNoModal";
import ActionBookCompleteBookButton from "../actionmenu/ActionBookCompleteBookButton";
import Serie from "../../model/books/Serie";
import HomeServerRoutes from "../../HomeServerRoutes";


const FullSerieComponent: FC = () => {

    const navigate = useNavigate();


    const { serieId } = useParams();

    const { allBooksSubState, dispatch, searchString, workInProgress } = useHomeServerContext();

    const [displayBookPopup, setDisplayBookPopup] = useState(false);

    const [focusedBook, setFocusedBook] = useState<Book>(Book.empty());

    const [displayDeletePopup, setDisplayDeletePopup] = useState(false);


    useEffect(() => {
        if (serieId) {
            dispatch(new StartWIPAction("Chargement de la série en cours"));
            BooksRequester.getSerie(serieId)
                .then(response => dispatch(new SelectedSerieLoadedAction(response)))
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement de la série")))
        }
    }, [serieId])

    useEffect(() => {
        if (allBooksSubState.selectedSerie.serie.id !== "") {
            dispatch(new StartWIPAction("Chargement des livres de la série en cours"))
            BooksRequester.getBooksFromSerie(allBooksSubState.selectedSerie.serie)
                .then(response => {
                    dispatch(new SelectedSerieBooksLoadedAction(response))
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement des livres de la série")))
        }

    }, [allBooksSubState.selectedSerie.serie]);


    const deleteBook = (bookToDelete: Book) => {
        BooksRequester.deleteBook(bookToDelete)
            .then(response => {
                dispatch(new EndWIPAction());
                setDisplayBookPopup(false);
                dispatch(new StartWIPAction("Chargement de la série en cours"));
                return BooksRequester.getSerie(allBooksSubState.selectedSerie.serie.id);
            })
            .then(response => dispatch(new SelectedSerieLoadedAction(response)))
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la suppression du livre ou du chargement de la série")))
    }

    const deleteSerie = () => {
        dispatch(new StartWIPAction("Demande de suppression de la série"));
        BooksRequester.deleteSerie(allBooksSubState.selectedSerie.serie)
            .then(response => {
                dispatch(new EndWIPAction());
                setDisplayDeletePopup(false);
                navigate("/books/allbooks");
            })
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la suppression de la série")));
    }

    const updateSerie = (completeStatus: boolean) => {
        dispatch(new StartWIPAction("Modification de la série en cours"));

        const updatedSerie: Serie = {
            ...allBooksSubState.selectedSerie.serie,
            completed: completeStatus
        }

        BooksRequester.updateSerie(updatedSerie)
            .then(response => {
                dispatch(new StartWIPAction("Rechargement de la série en cours"));
                return BooksRequester.getSerie(allBooksSubState.selectedSerie.serie.id);
            })
            .then(response => dispatch(new SelectedSerieLoadedAction(response)))
            .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la mise à jour ou de la récupération série")))
    }


    return <>
        <SimpleMarginLayout>
            <Card className="full-serie">
                <SerieCardHeader serieToDisplay={allBooksSubState.selectedSerie.serie} />
                <Card.Body>
                    <Row xs={2} md={2} lg={6}>
                        {allBooksSubState.selectedSerie.books.sort(Book.sorter())?.map((aBook, idx) => (
                            <Col key={idx}>
                                <Card className={CustomClassNames.Clickable} onClick={() => {
                                    setFocusedBook(aBook)
                                    setDisplayBookPopup(true);
                                }}>
                                    <Card.Img variant="top" loading="lazy" src={Book.bookPicture(aBook)} className="serie-picture" />
                                    <Card.Body>
                                        <CardText as="h3" className={BootstrapText.Capitalize}>{aBook.subtitle}</CardText>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))}
                    </Row>
                </Card.Body>
            </Card>
            <ActionMenuComponent alreadyOpen={true}>
                <ActionBackButton onClick={() => { navigate("/books/allbooks") }} />
                <ActionBookCompleteBookButton
                    serieToSwitch={allBooksSubState.selectedSerie.serie}
                    onCompleteSerieClick={() => { updateSerie(true) }}
                    onUnCompleteSerieClick={() => { updateSerie(false) }}
                />
                {allBooksSubState.selectedSerie.serie.completed ? <></> : <ActionBookPlusButton onClick={() => navigate(HomeServerRoutes.BOOKS_ADD_BOOK_TO_SELECTED_SERIE_ID(allBooksSubState.selectedSerie.serie.id))} />}
                <ActionDeleteButton onClick={() => setDisplayDeletePopup(true)} />
            </ActionMenuComponent>
        </SimpleMarginLayout>

        <Modal show={displayBookPopup} fullscreen={true} onHide={() => setDisplayBookPopup(false)}>
            <Modal.Header closeButton={!workInProgress}>
                <Modal.Title className="text-break-anywhere">{allBooksSubState.selectedSerie.serie.title} {focusedBook.subtitle}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Card className={CustomClassNames.Clickable}
                    onClick={() => setDisplayBookPopup(false)}
                >
                    <Card.Header
                        className={CssTools
                            .of("full-serie-header")
                            .css()}
                    >
                        <Image src={Book.bookPicture(focusedBook)} rounded></Image>
                        <div className="full-serie-header-labels">
                            <h1>
                                {focusedBook.title}
                            </h1>
                            <h3>
                                {focusedBook.subtitle}
                            </h3>
                            <h3>
                                <PersonCircle className={CustomClassNames.SpaceAfterIcon}></PersonCircle>{focusedBook.owner}
                            </h3>
                            <h6 className={BootstrapText.WordBreak}>
                                {focusedBook.id}
                            </h6>
                        </div>
                    </Card.Header>
                </Card>
            </Modal.Body>
            <Modal.Footer>
                <BlockingButton
                    label="Supprimer"
                    variant={BootstrapVariants.Danger}
                    onClick={() => { deleteBook(focusedBook) }}
                    icon={<Trash />}
                    toastMessage="Suppression du livre en cours"
                />
                <PassiveBlockingButton variant={BootstrapVariants.Primary} onClick={() => setDisplayBookPopup(false)} label="Annuler" />
            </Modal.Footer>
        </Modal>
        <YesNoModal
            question={`Voulez vous vraiment supprimer la série ${allBooksSubState.selectedSerie.serie.title} ?`}
            title="Supprimer une série"
            displayYesNoPopup={displayDeletePopup}
            onNoClick={() => setDisplayDeletePopup(false)}
            onYesClick={() => deleteSerie()}
        />
    </>
}

export default FullSerieComponent;