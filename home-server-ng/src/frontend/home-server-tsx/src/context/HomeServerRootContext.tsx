import {createContext, FC, useContext, useReducer} from "react";
import {HomeServerRootState} from "./states/HomeServerRootState";
import {HomeServerAction} from "./actions/HomeServerAction";
import AllLogEvents from "../model/administration/AllLogEvents";
import BooksGroupedBySeries from "../model/books/BooksGroupedBySeries";
import {BootstrapVariants} from "../components/bootstrap/BootstrapVariants";
import SerieWithItsBooks from "../model/books/SerieWithItsBooks";
import FileDirectoryDescriptor from "../model/filemanager/FileDirectoryDescriptor";
import SelectableItems from "../model/SelectableItems";
import {TodoSteps} from "../model/seedbox/TodoSteps";

const initialState: HomeServerRootState = {
    dispatch: () => { },
    helloWorld: "hello world",
    administrationSubState: {
        properties: [],
        tasks: [],
        allLogEvents: new AllLogEvents("", [])
    },
    workInProgress: false,
    searchString: "",
    toastSubState: {
        toastMessage: "",
        variant: BootstrapVariants.Primary
    },
    allBooksSubState: {
        booksGroupedBySeries: BooksGroupedBySeries.empty(),
        allSeries: [],
        selectedSerie: SerieWithItsBooks.empty()
    },
    fileManagerSubState: {
        rootDirectories: [],
        currentDirectory: FileDirectoryDescriptor.emptyFileDirectoryDescriptor(),
        directoriesFromCurrentDirectory: SelectableItems.empty(),
        filesFromCurrentDirectory: SelectableItems.empty(),
        directoriesStack: []
    },
    seedboxTodoSubState: {
        todoFiles: SelectableItems.empty(),
        targetDirectories: [],
        currentTargetDirectory: FileDirectoryDescriptor.emptyFileDirectoryDescriptor(),
        currentStep: TodoSteps.SELECT_TODO_FILES,
        backButtonDisabled: true,
        nextButtonDisabled: true,
        targetDirectoriesStack: [],
        filesToMove: []
    }

}

const reducer = (previousState: HomeServerRootState, action: HomeServerAction) => {
    return action.applyToState(previousState);
}

const HomeServerRootContext = createContext<HomeServerRootState>(initialState);

type HomeServerProviderProps = {
    children: any
}

const HomeServerProvider: FC<HomeServerProviderProps> = (props) => {
    const [state, dispatch] = useReducer(reducer, initialState);




    return <HomeServerRootContext.Provider value={{ ...state, dispatch }}>
        {props.children}
    </HomeServerRootContext.Provider>
}

const useHomeServerContext: () => HomeServerRootState = () => useContext(HomeServerRootContext);

export {
    HomeServerProvider,
    useHomeServerContext
}