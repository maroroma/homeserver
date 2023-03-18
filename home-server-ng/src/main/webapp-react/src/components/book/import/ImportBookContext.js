import React, {createContext, useContext, useReducer} from "react";
import {emptyDisplayList} from "../../../tools/displayList";
import {StepperDriver} from "../../commons/Stepper";
import SearchAndSelectSerieForBookComponent from "../add/SearchAndSelectSerieForBookComponent";
import FinalizeImportComponent from "./FinalizeImportComponent";
import ImportBookProposalsComponent from "./ImportBookProposalsComponent";
import SetUrlComponent from "./SetUrlComponent";
import bookApi from "../../../apiManagement/BookApi";
import enhance from "../../../tools/enhance";
import toaster from "../../commons/Toaster";

const initialState = {
    bookScrapperSource: "SANCTUARY",
    seriePageUrl: "",
    serie: {},
    owner: "",
    importTitlePrefix: "Tome",
    proposalResults: emptyDisplayList(),
    proposalResultsLoading: false,
    prefixForSubtitle: "",
    stepperDriver: new StepperDriver()
        .appendStep("playlist_add", "Sélectionner une série", <SearchAndSelectSerieForBookComponent canSkipSerie={false}></SearchAndSelectSerieForBookComponent>)
        .appendStep("edit", "Infos pour l'import", <SetUrlComponent></SetUrlComponent>)
        .appendStep("done_all", "Sélection des livres", <ImportBookProposalsComponent></ImportBookProposalsComponent>)
        .appendStep("file_upload", "Finalisation de l'import", <FinalizeImportComponent></FinalizeImportComponent>)
        .withSelected(0)
        .disableNextStepButton()
        .disableAfterSelected()
};

const ImportActions = {
    UPDATED_URL: "UPDATED_URL",
    EXECUTE_LOAD_PROPOSALS: "EXECUTE_LOAD_PROPOSALS",
    PROPOSALS_LOADED: "PROPOSALS_LOADED",
    SELECT_SERIE: "SELECT_SERIE",
    SELECT_ONE_PROPOSAL: "SELECT_ONE_PROPOSAL",
    STEP_DRIVER_EXTERNAL_UPDATE: "STEP_DRIVER_EXTERNAL_UPDATE",
    UPDATE_IMPORT_PREFIX: "UPDATE_IMPORT_PREFIX",
    UPDATE_OWNER: "UPDATE_OWNER",
    EXECUTE_IMPORT: "EXECUTE_IMPORT",
    IMPORT_COMPLETED: "IMPORT_COMPLETED"
};


const reducer = (previousState, action) => {
    switch (action.type) {
        case ImportActions.SELECT_SERIE:
            return updateSelectedSerie(previousState, action);
        case ImportActions.UPDATED_URL:
            return updateUrl(previousState, action);
        case ImportActions.STEP_DRIVER_EXTERNAL_UPDATE:
            return externalUpdateForStepDriver(previousState, action);
        case ImportActions.EXECUTE_LOAD_PROPOSALS:
            return executeLoadProposals(previousState, action);
        case ImportActions.PROPOSALS_LOADED:
            return loadNewProposals(previousState, action);
        case ImportActions.SELECT_ONE_PROPOSAL:
            return bookProposalSelected(previousState, action);
        case ImportActions.UPDATE_IMPORT_PREFIX:
            return updateImportPrefix(previousState, action);
        case ImportActions.UPDATE_OWNER:
            return updateOwner(previousState, action);
        case ImportActions.EXECUTE_IMPORT:
            return executeImport(previousState, action);
        case ImportActions.IMPORT_COMPLETED:
            return importCompleted(previousState, action);
        default:
            return previousState;
    }
};

const ImportBookContext = createContext();

const ImportBookProvider = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState);

    const value = {
        bookScrapperSource: "SANCTUARY",
        seriePageUrl: state.seriePageUrl,
        serie: state.serie,
        owner: state.owner,
        importTitlePrefix: state.importTitlePrefix,
        proposalResults: state.proposalResults,
        prefixForSubtitle: state.prefixForSubtitle,
        stepperDriver: state.stepperDriver,
        proposalResultsLoading: state.proposalResultsLoading,

        dispatchSerieUpdated: (selectedSerie) => {
            dispatch({ type: ImportActions.SELECT_SERIE, selectedSerie: selectedSerie });
        },
        dispatchNewUrl: (newUrl) => {
            dispatch({ type: ImportActions.UPDATED_URL, seriePageUrl: newUrl });
        },
        dispatchExternalUpdateForStepDriver: (newStepDriver) => {
            dispatch({ type: ImportActions.STEP_DRIVER_EXTERNAL_UPDATE, newStepDriver: newStepDriver, dispatch: dispatch });
        },
        dispatchSelectOneProposal: (bookProposal) => {
            dispatch({ type: ImportActions.SELECT_ONE_PROPOSAL, bookProposalWithSelectionChanged: bookProposal })
        },
        dispatchNewImportPrefix: (newImportPrefix) => {
            dispatch({ type: ImportActions.UPDATE_IMPORT_PREFIX, newImportPrefix: newImportPrefix })
        },
        dispatchNewOwner: (newOwner) => {
            dispatch({ type: ImportActions.UPDATE_OWNER, newOwner: newOwner })
        },
        dispatchExecuteImport: () => {
            dispatch({ type: ImportActions.EXECUTE_IMPORT, dispatch: dispatch });
        }
    };

    return <ImportBookContext.Provider value={value}>
        {children}
    </ImportBookContext.Provider>
}

const useImportBookContext = () => useContext(ImportBookContext);


const updateSelectedSerie = (previousState, action) => {
    return {
        ...previousState,
        serie: action.selectedSerie,
        stepperDriver: { ...previousState.stepperDriver.nextStep() }
    }
}

const updateUrl = (previousState, action) => {

    const newStepperDriver = action.seriePageUrl !== "" ? { ...previousState.stepperDriver.enableNextStepButton() } : { ...previousState.stepperDriver.disableNextStepButton() }

    return {
        ...previousState,
        seriePageUrl: action.seriePageUrl,
        stepperDriver: { ...newStepperDriver }
    }
}

const externalUpdateForStepDriver = (previousState, action) => {
    if (action.newStepDriver.getSelectedIndex() === 2) {
        action.dispatch({ type: ImportActions.EXECUTE_LOAD_PROPOSALS, dispatch: action.dispatch });
    }

    return { ...previousState, stepperDriver: { ...action.newStepDriver } };
}

const executeLoadProposals = (previousState, action) => {


    bookApi().findBooksBySerieResource(previousState)
        .then(result => {
            action.dispatch({ type: ImportActions.PROPOSALS_LOADED, result: result });
        });


    return { ...previousState, proposalResultsLoading: true };
}

const executeImport = (previousState, action) => {

    toaster().plopAndWait(() => bookApi().importBooksIntoSerie({
        targetedSerie: previousState.serie,
        importPrefix: previousState.importTitlePrefix,
        owner: previousState.owner,
        booksToImport: previousState.proposalResults.getSelectedItems()
    }), "import en cours")
        .then(result => {
            action.dispatch({ type: ImportActions.IMPORT_COMPLETED });
        });


    return { ...previousState };
}

const loadNewProposals = (previousState, action) => {
    return {
        ...previousState,
        proposalResultsLoading: false,
        proposalResults: {
            ...previousState.proposalResults.update(action.result)
                .updateItems(enhance().selectable())
                .updateItems(enhance().indexed())
        }
    };
}

const bookProposalSelected = (previousState, action) => {


    const newProposalResults = {
        ...previousState
            .proposalResults
            .updateSelectableItems(action.bookProposalWithSelectionChanged.index, !action.bookProposalWithSelectionChanged.selected, item => item.index)
    };


    // ici : controller qu'on peut bien activer l'étape suivante
    const newStepDriver = newProposalResults.getSelectedItems().length === 0 ? { ...previousState.stepperDriver.disableNextStepButton() } : { ...previousState.stepperDriver.enableNextStepButton() };


    return {
        ...previousState,
        proposalResults: newProposalResults,
        stepperDriver: newStepDriver
    }
}

const updateImportPrefix = (previousState, action) => {
    return {
        ...previousState,
        importTitlePrefix: action.newImportPrefix
    };
}

const importCompleted = (previousState, action) => {
    return { ...initialState, stepperDriver: previousState.stepperDriver.withSelected(0) };
}

const updateOwner = (previousState, action) => {
    return {
        ...previousState,
        owner: action.newOwner
    }
}

export {
    ImportBookContext,
    ImportBookProvider,
    ImportActions,
    useImportBookContext
}