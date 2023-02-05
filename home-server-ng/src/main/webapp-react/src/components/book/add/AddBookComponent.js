import React, { useEffect, useRef, useState } from 'react';
import bookApi from '../../../apiManagement/BookApi';
import { StepperComponent, StepperDriver } from '../../commons/Stepper';
import { addBookSubReactor } from './AddBookSubReactor';
import ConfirmBookAddComponent from './ConfirmBookAddComponent';
import SearchAndSelectSerieForBookComponent from './SearchAndSelectSerieForBookComponent';
import SearchBookForAddComponent from './SearchBookForAddComponent';
import SearchForAddResultsComponent from './SearchForAddResultsComponent';


export default function AddBookComponent() {



    const [stepperDriver, setStepperDriver] = useState(new StepperDriver()
        .appendStep("search", "Récupérer l'ISBN", <SearchBookForAddComponent></SearchBookForAddComponent>)
        .appendStep("library_books", "Résultats de la recherche", <SearchForAddResultsComponent></SearchForAddResultsComponent>)
        .appendStep("playlist_add", "Sélectionner une série", <SearchAndSelectSerieForBookComponent></SearchAndSelectSerieForBookComponent>)
        .appendStep("done", "Confirmer l'ajout", <ConfirmBookAddComponent></ConfirmBookAddComponent>)
        .withSelected(0)
        .disableAfterSelected()
        .enableNextStepButton());

    // maintient l'état de la requête que l'on va envoyer pour rajouter le book en base
    const [bookToAddRequest, setBookToAddRequest] = useState(
        {
            booksToAdd: [],
            serieToAssociateTo: {}
        }
    );

    const updateBookToAddRequest = (newBookRequest) => {
        setBookToAddRequest(newBookRequest);
        addBookSubReactor().bookAddRequestUpdated(newBookRequest);
    }




    useEffect(() => {


        // recherche effectuée, passage à la sélection des livres
        const unsubscribeSearchResultReceived = addBookSubReactor().onSearchResultReceived(result => {
            setStepperDriver({ ...stepperDriver.nextStep().disableAfterSelected() });
        });

        // pas de sélection de série, passage à la confirmation
        const unsubscribeSkipSerie = addBookSubReactor().onSkipSerie(result => {
            // maj de la requête
            updateBookToAddRequest({ ...bookToAddRequest, serieToAssociateTo: null })
            setStepperDriver({ ...stepperDriver.nextStep().enableNextStepButton() });

        });

        // sélection d'un livre, passage à l'étape suivante (sélection d'une série)
        const unsubscribeBookToAddCandidateUpdated = addBookSubReactor().onBookToAddCandidateUpdated(selectedBooks => {

            // maj de la requête
            console.log("onBookToAddCandidateUpdated", selectedBooks);
            updateBookToAddRequest({ ...bookToAddRequest, booksToAdd: selectedBooks })


            // maj du stepper
            if (selectedBooks.length === 0) {
                setStepperDriver({ ...stepperDriver.disableNextStepButton().disableAfterSelected() });
            } else {
                setStepperDriver({ ...stepperDriver.nextStep().enableNextStepButton() });
            }
        });

        // sélection d'une série, passage à l'écran de confirmation
        const unsubscribeSerieSelected = addBookSubReactor().onSerieSelected(selectedSerie => {
            updateBookToAddRequest({ ...bookToAddRequest, serieToAssociateTo: selectedSerie });

            // maj du stepper
            setStepperDriver({ ...stepperDriver.nextStep().enableNextStepButton() });
        });

        const unsubscribeUpdateDriver = stepperDriver.onDriverUpdated((newDriver) => {
            console.log("onDriverUpdated", newDriver.steps.rawList);
            setStepperDriver({ ...newDriver });
        });

        const unsubscribeFinalRequestUpdated = addBookSubReactor().onBookAddRequestUpdated(newRequest => {
            setBookToAddRequest(newRequest);
        });

        // ajout du / des livres en base suite à la validation de la dernière étape du stepper
        const unsubscribeStepperCompleted = stepperDriver.onComplete(() => {
            bookApi()
                .addBooksToLibrary({
                    booksToAdd: bookToAddRequest.booksToAdd,
                    serieIdToAssociateTo: bookToAddRequest.serieToAssociateTo?.id
                })
                .then(result => {
                    setStepperDriver({ ...stepperDriver.withSelected(0).disableAfterSelected() });
                })
        });

        return () => {
            unsubscribeSearchResultReceived();
            unsubscribeUpdateDriver();
            unsubscribeBookToAddCandidateUpdated();
            unsubscribeSkipSerie();
            unsubscribeSerieSelected();
            unsubscribeStepperCompleted();
            unsubscribeFinalRequestUpdated();
        }


    }, [bookToAddRequest]);


    return (
        <StepperComponent driver={stepperDriver} stepperId="addBookStepper">
        </StepperComponent>
    )
}