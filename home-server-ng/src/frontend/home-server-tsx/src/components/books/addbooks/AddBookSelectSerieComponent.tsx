import {FC, useEffect, useState} from "react";
import {useHomeServerContext} from "../../../context/HomeServerRootContext";
import StartWIPAction from "../../../context/actions/StartWIPAction";
import BooksRequester from "../../../api/BooksRequester";
import {SeriesLoadedAction} from "../../../context/actions/books/SeriesLoadedAction";
import ToastAction from "../../../context/actions/ToastAction";
import {Form, InputGroup, ListGroup} from "react-bootstrap";
import Serie from "../../../model/books/Serie";
import {BootstrapText} from "../../bootstrap/BootstrapText";
import Thumb from "../../thumb/Thumb";
import {useNavigate} from "react-router-dom";
import EndWIPInErrorAction from "../../../context/actions/EndWIPInErrorAction";
import SimpleMarginLayout from "../../layouts/SimpleMarginLayout";
import UpdateSearchStringAction from "../../../context/actions/UpdateSearchStringAction";
import BlockingButton from "../../blockingbutton/BlockingButton";
import {PlusCircle} from "react-bootstrap-icons";
import EndWIPAction from "../../../context/actions/EndWIPAction";

const AddBookSelectSerieComponent: FC = () => {


    const { allBooksSubState, dispatch, searchString } = useHomeServerContext();

    const navigate = useNavigate();

    const [filteredExistingSeries, setFilteredExistingSeries] = useState<Serie[]>([]);

    const [newSerieName, setNewSerieName] = useState("");


    useEffect(() => {
        if (allBooksSubState.allSeries.length === 0) {
            dispatch(new StartWIPAction("Séries en cours de chargement"));

            BooksRequester.getAllSeries()
                .then(response => dispatch(new SeriesLoadedAction(response)))
                .catch(error => dispatch(new EndWIPInErrorAction("erreur rencontrée lors du charment des séries")))
        } else {
            setFilteredExistingSeries(allBooksSubState.allSeries.filter(Serie.filter(searchString)));
        }
    }, [allBooksSubState.allSeries, searchString])

    const addNewSerie = () => {
        BooksRequester.createSerie(newSerieName)
        .then(response => {
            dispatch(new EndWIPAction());
            const createdSerie = response.find(Serie.equalsByTitle(newSerieName));
            if (createdSerie) {
                dispatch(UpdateSearchStringAction.clear());
                navigate(`/books/add/serie/${createdSerie.id}`);
            } else {
                dispatch(ToastAction.error("Impossible de retrouver la série créée"));
            }
        })
        .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors de la création de la nouvelle série")))
    }


    return <SimpleMarginLayout>
        <h1>Sélectionnez une série</h1>
        <ListGroup>
            <ListGroup.Item>
                <InputGroup size="lg">
                    <InputGroup.Text id="inputGroup-sizing-lg">Nouvelle Série</InputGroup.Text>
                    <Form.Control
                        aria-label="Large"
                        aria-describedby="inputGroup-sizing-sm"
                        value={newSerieName}
                        onChange={(event) => setNewSerieName(event.target.value)}
                    />
                    <BlockingButton
                        icon={<PlusCircle />}
                        label=""
                        disabled={newSerieName === undefined || newSerieName === ""}
                        toastMessage="Création de la série en cours"
                        onClick={() => addNewSerie()}
                    />
                </InputGroup>
            </ListGroup.Item>
            {filteredExistingSeries
                .map((aSerie, index) => <ListGroup.Item
                    key={`selectable-serie${index}`}
                    action
                    className="serie-title"
                    onClick={() => {
                        dispatch(UpdateSearchStringAction.clear());
                        navigate(`/books/add/serie/${aSerie.id}`);
                    }}
                >
                    <Thumb src={Serie.seriePicture(aSerie)} />
                    <span className={BootstrapText.UpperCase}>{aSerie.title}</span>
                </ListGroup.Item>)}
        </ListGroup>
    </SimpleMarginLayout>
}

export default AddBookSelectSerieComponent;