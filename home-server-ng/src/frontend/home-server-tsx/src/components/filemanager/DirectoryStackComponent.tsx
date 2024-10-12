import {FC} from "react";
import {Breadcrumb, BreadcrumbItem} from "react-bootstrap";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import {BootstrapText} from "../bootstrap/BootstrapText";
import FileDescriptor from "../../model/filemanager/FileDescriptor";
import StartWIPAction from "../../context/actions/StartWIPAction";
import FileManagerRequester from "../../api/FileManagerRequester";
import LoadedDirectoryAction from "../../context/actions/filemanager/LoadedDirectoryAction";
import EndWIPInErrorAction from "../../context/actions/EndWIPInErrorAction";
import FileDirectoryDescriptor from "../../model/filemanager/FileDirectoryDescriptor";
import LoadedRootDirectoriesAction from "../../context/actions/filemanager/LoadedRootDirectoriesAction";

const DirectoryStackComponent: FC = () => {


    const { fileManagerSubState, dispatch } = useHomeServerContext();

    const loadSubDirectory = (aDirectory: FileDescriptor) => {
        dispatch(new StartWIPAction("Chargement du répertoire"));

        if (FileDirectoryDescriptor.isRoot(aDirectory)) {
            FileManagerRequester.getRootDirectories()
                .then(response => dispatch(new LoadedRootDirectoriesAction(response)))
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur recontrée lors du chargement des répertoires racines")))
        } else {
            FileManagerRequester.getDirectoryDetails(aDirectory)
                .then(response => {
                    dispatch(new LoadedDirectoryAction(response));
                })
                .catch(error => dispatch(new EndWIPInErrorAction("Erreur rencontrée lors du chargement du répertoire")))
        }
    }

    return <><Breadcrumb>
        {
            fileManagerSubState.directoriesStack
                .map((aDirectory, index) => <BreadcrumbItem
                    key={index}
                    active={index === fileManagerSubState.directoriesStack.length - 1}
                    className={BootstrapText.UpperCase}
                    onClick={() => loadSubDirectory(aDirectory)}

                >
                    {aDirectory.name}
                </BreadcrumbItem>)
        }

    </Breadcrumb></>

}

export default DirectoryStackComponent;