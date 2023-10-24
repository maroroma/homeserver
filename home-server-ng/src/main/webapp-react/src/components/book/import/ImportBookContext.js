import React, {createContext, useContext, useReducer} from "react";
import {emptyDisplayList} from "../../../tools/displayList";
import FinalizeImportComponent from "./FinalizeImportComponent";
import ImportBookProposalsComponent from "./ImportBookProposalsComponent";
import SetUrlComponent from "./SetUrlComponent";
import bookApi from "../../../apiManagement/BookApi";
import enhance from "../../../tools/enhance";
import toaster from "../../commons/Toaster";
import {stepperConfiguration} from "../../commons/SimpleStepper/SimpleStepper";
import SelectSerieForImportComponent from "./SelectSerieForImportComponent";
import sort from "../../../tools/sort";
import on from "../../../tools/on";

const ImportSteps = {
    SELECT_A_SERIE: 0,
    SET_URL_FOR_IMPORT: 1,
    SELECT_BOOK_TO_IMPORT: 2,
    FINALIZE_IMPORT: 3
};

const ImportActions = {
    EXECUTE_LOAD_SERIES: "EXECUTE_LOAD_SERIES",
    SERIES_LOADED: "SERIES_LOADED",
    SEARCH_SERIES: "SEARCH_SERIES",
    UPDATED_URL: "UPDATED_URL",
    EXECUTE_LOAD_PROPOSALS: "EXECUTE_LOAD_PROPOSALS",
    PROPOSALS_LOADED: "PROPOSALS_LOADED",
    SELECT_SERIE: "SELECT_SERIE",
    SELECT_ONE_PROPOSAL: "SELECT_ONE_PROPOSAL",
    UPDATE_IMPORT_PREFIX: "UPDATE_IMPORT_PREFIX",
    UPDATE_OWNER: "UPDATE_OWNER",
    EXECUTE_IMPORT: "EXECUTE_IMPORT",
    IMPORT_COMPLETED: "IMPORT_COMPLETED",
    NEXT_STEP_REQUIRED: "NEXT_STEP_REQUIRED",
    PREVIOUS_STEP_REQUIRED: "PREVIOUS_STEP_REQUIRED",
    MANUAL_STEP_REQUIRED: "MANUAL_STEP_REQUIRED",
    ADD_NEW_SERIE: "ADD_NEW_SERIE",
};


const generateInitialState = () => {
    return {
        bookScrapperSource: "SANCTUARY",
        seriePageUrl: "",
        serie: {},
        owner: "",
        importTitlePrefix: "Tome",
        allSeries: emptyDisplayList(),
        searchSeriesFilter: "",
        proposalResults: emptyDisplayList(),
        proposalResultsLoading: false,
        prefixForSubtitle: "",
        stepperConfiguration: stepperConfiguration().initialize(
            [
                {
                    icon: "playlist_add",
                    title: "Sélectionner une série",
                    component: <SelectSerieForImportComponent></SelectSerieForImportComponent>
                },
                {
                    icon: "edit",
                    title: "Infos pour l'import",
                    component: <SetUrlComponent></SetUrlComponent>
                },
                {
                    icon: "done_all",
                    title: "Sélection des livres",
                    component: <ImportBookProposalsComponent></ImportBookProposalsComponent>
                },
                {
                    icon: "file_upload",
                    title: "Finalisation de l'import",
                    component: <FinalizeImportComponent></FinalizeImportComponent>
                },
            ])
    };
}






const reducer = (previousState, action) => {
    console.log("ImportBookContext Action", action);
    switch (action.type) {
        case ImportActions.SELECT_SERIE:
            return updateSelectedSerie(previousState, action);
        case ImportActions.UPDATED_URL:
            return updateUrl(previousState, action);
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
        case ImportActions.NEXT_STEP_REQUIRED:
            return checkNextStep(previousState, action);
        case ImportActions.PREVIOUS_STEP_REQUIRED:
            return checkPreviousStep(previousState, action);
        case ImportActions.EXECUTE_LOAD_SERIES:
            return executeLoadSeries(previousState, action);
        case ImportActions.SERIES_LOADED:
            return loadSeries(previousState, action);
        case ImportActions.SEARCH_SERIES:
            return searchSeries(previousState, action);
        case ImportActions.ADD_NEW_SERIE:
            return addNewSerie(previousState, action);
        case ImportActions.MANUAL_STEP_REQUIRED:
            return checkManualStep(previousState, action);

        default:
            return previousState;
    }
};

const ImportBookContext = createContext();

const ImportBookProvider = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, generateInitialState());

    const value = {
        bookScrapperSource: "SANCTUARY",
        seriePageUrl: state.seriePageUrl,
        serie: state.serie,
        owner: state.owner,
        importTitlePrefix: state.importTitlePrefix,
        proposalResults: state.proposalResults,
        prefixForSubtitle: state.prefixForSubtitle,
        proposalResultsLoading: state.proposalResultsLoading,
        stepperConfiguration: state.stepperConfiguration,
        allSeries: state.allSeries,
        searchSeriesFilter: state.searchSeriesFilter,

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
        },
        dispatchExecuteLoadProposals: () => {
            dispatch({ type: ImportActions.EXECUTE_LOAD_PROPOSALS, dispatch: dispatch })
        },
        dispatchNextStepRequired: () => {
            dispatch({ type: ImportActions.NEXT_STEP_REQUIRED, dispatch: dispatch });
        },
        dispatchPreviousStepRequired: () => {
            dispatch({ type: ImportActions.PREVIOUS_STEP_REQUIRED, dispatch: dispatch });
        },
        dispatchManualStepRequired: (selectedStep) => {
            dispatch({ type: ImportActions.MANUAL_STEP_REQUIRED, dispatch: dispatch, selectedStep: selectedStep });
        },
        dispatchLoadAllSeries: () => {
            dispatch({ type: ImportActions.EXECUTE_LOAD_SERIES, dispatch: dispatch });
        },
        dispatchSearchSeries: (searchSeriesFilter) => {
            dispatch({ type: ImportActions.SEARCH_SERIES, searchSeriesFilter: searchSeriesFilter });
        },
        dispatchAddNewSerie: (newSerieName) => {
            dispatch({ type: ImportActions.ADD_NEW_SERIE, newSerieName: newSerieName, dispatch: dispatch });
        }
    };

    return <ImportBookContext.Provider value={value}>
        {children}
    </ImportBookContext.Provider>
}

const useImportBookContext = () => useContext(ImportBookContext);

const isUrlForSerieSet = (url) => {
    return (url !== "" && url !== undefined && url !== null);
}

const updateSelectedSerie = (previousState, action) => {
    const isUrlSet = isUrlForSerieSet(action.selectedSerie.serieUrlForImport);


    const stepperWithUrlSelectionStepSelected = stepperConfiguration().selectStep(previousState.stepperConfiguration, ImportSteps.SET_URL_FOR_IMPORT);


    const stepperWithBookSelectionStepActivated = isUrlSet ?
        stepperConfiguration().enableStep(stepperWithUrlSelectionStepSelected, ImportSteps.SELECT_BOOK_TO_IMPORT)
        : stepperConfiguration().disableStep(stepperWithUrlSelectionStepSelected, ImportSteps.SELECT_BOOK_TO_IMPORT);


    return {
        ...previousState,
        serie: action.selectedSerie,
        seriePageUrl: action.selectedSerie.serieUrlForImport === null ? "" : action.selectedSerie.serieUrlForImport,
        stepperConfiguration: stepperWithBookSelectionStepActivated
    }
}

const updateUrl = (previousState, action) => {

    const isUrlSet = isUrlForSerieSet(action.seriePageUrl);

    const stepperWithBookSelectionStepActivated = isUrlSet ?
        stepperConfiguration().enableStep(previousState.stepperConfiguration, ImportSteps.SELECT_BOOK_TO_IMPORT)
        : stepperConfiguration().disableStep(previousState.stepperConfiguration, ImportSteps.SELECT_BOOK_TO_IMPORT);


    return {
        ...previousState,
        seriePageUrl: action.seriePageUrl,
        stepperConfiguration: stepperWithBookSelectionStepActivated
    }
}

const executeLoadProposals = (previousState, action) => {

    const loadProposalsRequest = {
        bookScrapperSource: previousState.bookScrapperSource,
        seriePageUrl: previousState.seriePageUrl,
        serie: previousState.serie
    };

    bookApi().findBooksBySerieResource(loadProposalsRequest)
        .then(result => {
            console.log("coucou j'ai fini de charger")
            action.dispatch({ type: ImportActions.PROPOSALS_LOADED, result: result });
        });


    return { ...previousState, proposalResultsLoading: true };
}

const executeLoadSeries = (previousState, action) => {

    bookApi().getAllSeries()
        .then(result => {
            action.dispatch({ type: ImportActions.SERIES_LOADED, result: result });
        });

    return { ...previousState };
}


const loadSeries = (previousState, action) => {
    return {
        ...previousState,
        // on vire les séries complétées
        allSeries: previousState.allSeries.update(action.result.filter(oneSerie => !oneSerie.completed))
            .updateItems(enhance().selectable())
            .updateSort(sort().basic(oneSerie => oneSerie.title))
    };
}

const searchSeries = (previousState, action) => {
    return {
        ...previousState,
        searchSeriesFilter: action.searchSeriesFilter,
        allSeries: previousState.allSeries.updateFilter(
            on()
                .stringContains(action.searchSeriesFilter, oneSerie => oneSerie.title)
        )
    };
}

const addNewSerie = (previousState, action) => {
    bookApi().addNewSerie(action.newSerieName)
        .then(result => {
            action.dispatch({ type: ImportActions.SERIES_LOADED, result: result });
            action.dispatch({ type: ImportActions.SELECT_SERIE, selectedSerie: result.find(oneItem => oneItem.title === action.newSerieName) });
        }, () => console.log("hophophoph tu peux paaaaaaaaaaaaaas"));

    return {
        ...previousState
    }
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


    const stepperConfigurationWithBookSelectionSelected = stepperConfiguration().selectStep(previousState.stepperConfiguration, ImportSteps.SELECT_BOOK_TO_IMPORT);
    const stepperConfigurationWithDisabledNextStep = stepperConfiguration().disableStep(stepperConfigurationWithBookSelectionSelected, ImportSteps.FINALIZE_IMPORT);
    const stepperConfigurationWithDisabledCompleteAction = stepperConfiguration().disableCompleteAction(stepperConfigurationWithDisabledNextStep);

    return {
        ...previousState,
        proposalResultsLoading: false,
        proposalResults: {
            ...previousState.proposalResults.update(action.result)
                .updateItems(enhance().selectable())
                .updateItems(enhance().indexed())
        },
        stepperConfiguration: stepperConfigurationWithDisabledCompleteAction
    };
}

const bookProposalSelected = (previousState, action) => {


    const newProposalResults = {
        ...previousState
            .proposalResults
            .updateSelectableItems(action.bookProposalWithSelectionChanged.index, !action.bookProposalWithSelectionChanged.selected, item => item.index)
    };


    // ici : controller qu'on peut bien activer l'étape suivante
    const someProposalsAreSelected = newProposalResults.getSelectedItems().length !== 0;


    const stepperConfigurationWithFinalizeStepStatus = someProposalsAreSelected ?
        stepperConfiguration().enableStep(previousState.stepperConfiguration, ImportSteps.FINALIZE_IMPORT) :
        stepperConfiguration().disableStep(previousState.stepperConfiguration, ImportSteps.FINALIZE_IMPORT);

    const stepperConfigurationWithCompleteActionStatus = someProposalsAreSelected ?
        stepperConfiguration().enableCompleteAction(stepperConfigurationWithFinalizeStepStatus) :
        stepperConfiguration().disableCompleteAction(stepperConfigurationWithFinalizeStepStatus);

    return {
        ...previousState,
        proposalResults: newProposalResults,
        stepperConfiguration: stepperConfigurationWithCompleteActionStatus
    }
}

const updateImportPrefix = (previousState, action) => {
    return {
        ...previousState,
        importTitlePrefix: action.newImportPrefix
    };
}

const importCompleted = (previousState, action) => {
    return {
        ...generateInitialState(),
        allSeries: previousState.allSeries
    };
}

const updateOwner = (previousState, action) => {
    return {
        ...previousState,
        owner: action.newOwner
    }
}

const checkNextStep = (previousState, action) => {
    if (previousState.stepperConfiguration.selectedIndex === ImportSteps.SET_URL_FOR_IMPORT) {
        action.dispatch({ type: ImportActions.EXECUTE_LOAD_PROPOSALS, dispatch: action.dispatch });
        return {
            ...previousState
        };
    }

    if (previousState.stepperConfiguration.selectedIndex === ImportSteps.SELECT_BOOK_TO_IMPORT) {
        return {
            ...previousState,
            stepperConfiguration: stepperConfiguration().selectStep(previousState.stepperConfiguration, ImportSteps.FINALIZE_IMPORT)
        };
    }

    return {
        ...previousState
    };
}

const checkPreviousStep = (previousState, action) => {
    if (previousState.stepperConfiguration.selectedIndex === ImportSteps.FINALIZE_IMPORT) {
        action.dispatch({ type: ImportActions.EXECUTE_LOAD_PROPOSALS, dispatch: action.dispatch });
        return {
            ...previousState
        };
    }

    if (previousState.stepperConfiguration.selectedIndex === ImportSteps.SELECT_BOOK_TO_IMPORT) {
        return {
            ...previousState,
            stepperConfiguration: stepperConfiguration().selectStep(previousState.stepperConfiguration, ImportSteps.SET_URL_FOR_IMPORT)
        };
    }

    if (previousState.stepperConfiguration.selectedIndex === ImportSteps.SET_URL_FOR_IMPORT) {

        const stepperConfigurationWithDisabledNextStep = stepperConfiguration().disableStep(previousState.stepperConfiguration, ImportSteps.SET_URL_FOR_IMPORT);



        return {
            ...previousState,
            serie: {},
            seriePageUrl: "",
            stepperConfiguration: stepperConfiguration().selectStep(stepperConfigurationWithDisabledNextStep, ImportSteps.SELECT_A_SERIE)
        };
    }

    return {
        ...previousState
    };
}

const checkManualStep = (previousState, action) => {

    // retour arrière, on désactive tout
    if (action.selectedStep.index === ImportSteps.SELECT_A_SERIE) {
        return importCompleted(previousState);
    }

    if (action.selectedStep.index === ImportSteps.SET_URL_FOR_IMPORT) {
        return updateSelectedSerie(previousState, {
            selectedSerie: previousState.serie
        });
    }

    if (action.selectedStep.index === ImportSteps.SELECT_BOOK_TO_IMPORT) {
        action.dispatch({ type: ImportActions.EXECUTE_LOAD_PROPOSALS, dispatch: action.dispatch });
        return {
            ...previousState
        };
    }


    return {
        ...previousState
    };
}


export {
    ImportBookContext,
    ImportBookProvider,
    ImportActions,
    useImportBookContext
}