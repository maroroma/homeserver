import {FC, useEffect, useState} from "react";
import BooksRequester from "../../api/BooksRequester";
import {Col, Row} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import StartWIPAction from "../../context/actions/StartWIPAction";

import "./books.css"
import SimpleSerieWithBooksRenderer from "./SimpleSerieWithBooksRenderer";
import Serie from "../../model/books/Serie";
import {SeriesLoadedAction} from "../../context/actions/books/SeriesLoadedAction";
import ToastAction from "../../context/actions/ToastAction";
import SimpleMarginLayout from "../layouts/SimpleMarginLayout";
import ActionMenuComponent from "../actionmenu/ActionMenuComponent";
import ActionBookSendButton from "../actionmenu/ActionBookSendButton";
import SendCollectionsStatusRequest from "../../model/books/SendCollectionsStatusRequest";
import EndWIPAction from "../../context/actions/EndWIPAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";

const AllBooksComponent: FC = () => {



    const { dispatch, allBooksSubState, searchString } = useHomeServerContext();

    const [series, setSeries] = useState<Serie[]>([]);


    useEffect(() => {

        dispatch(new StartWIPAction("Séries en cours de chargement"));

        BooksRequester.getAllSeries()
            .then(response => dispatch(new SeriesLoadedAction(response)))
            .catch(error => dispatch(ToastAction.error("erreur rencontrée lors du charment des séries")))

    }, []);

    useEffect(() => {

        setSeries(allBooksSubState.allSeries.filter(Serie.filter(searchString)).sort(Serie.sorter()));

    }, [allBooksSubState.allSeries, searchString])


    const sendEmails = () => {
        dispatch(new StartWIPAction("Envoi des tomes manquants à toute la famille"));

        BooksRequester.sendEmailForMissingBooks(SendCollectionsStatusRequest.default())
            .then(response => dispatch(new EndWIPAction()))
            .catch(error => dispatch(new EndWIPInErrorAction("Une erreur est survenur lors de l'envoi du mail")));
    }




    return <SimpleMarginLayout>
        <Row xs={2} md={2} lg={6}>
            {series.map((aSerie, idx) => (
                <Col key={idx}>
                    <SimpleSerieWithBooksRenderer aSerieWithItsBooks={aSerie} />
                </Col>
            ))}
        </Row>
        <ActionMenuComponent alreadyOpen={true}>
            <ActionBookSendButton blockingButton={true} onClick={() => sendEmails()}/>
        </ActionMenuComponent>
    </SimpleMarginLayout>
}

export default AllBooksComponent;